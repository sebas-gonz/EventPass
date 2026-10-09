package com.seb.eventpass.users.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.seb.eventpass.users.dto.requests.CreateUserDTO;
import com.seb.eventpass.users.dto.requests.LoginRequestDTO;
import com.seb.eventpass.users.dto.responses.TokenResponseDTO;
import com.seb.eventpass.users.models.UserEventPass;
import com.seb.eventpass.users.models.UserRole;
import com.seb.eventpass.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${SECRET_TOKEN_KEY}")
    private String SECRET_KEY;

    private final Long EXPIRATION_TIME = 864_000_000L;
    @Override
    public Mono<UserEventPass> register(CreateUserDTO createUserDTO) {
        return userRepository.findByEmail(createUserDTO.email())
                .hasElement()
                .flatMap(exist -> {
                    if (exist) {
                        return Mono.error(new IllegalStateException("User with email already exists"));
                    }

                    UserEventPass userEventPass = UserEventPass.builder()
                            .firstname(createUserDTO.firstname())
                            .lastname(createUserDTO.lastname())
                            .password(passwordEncoder.encode(createUserDTO.password()))
                            .email(createUserDTO.email())
                            .role(UserRole.ROLE_USER.toString())
                            .build();
                    return userRepository.save(userEventPass);
                });
    }

    @Override
    public Mono<TokenResponseDTO> login(LoginRequestDTO loginRequestDTO) {
        return userRepository.findByEmail(loginRequestDTO.email())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Credenciales inválidas")))
                .flatMap(user -> {
                    if (passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {

                        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
                        String token = JWT.create()
                                .withSubject(user.getUuid().toString())
                                .withClaim("role", user.getRole())
                                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                                .sign(algorithm);

                        return Mono.just(new TokenResponseDTO(token));
                    } else {
                        return Mono.error(new IllegalArgumentException("Credenciales inválidas"));
                    }
                });
    }

}

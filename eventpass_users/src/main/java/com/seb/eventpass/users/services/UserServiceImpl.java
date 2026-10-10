package com.seb.eventpass.users.services;

import com.seb.eventpass.users.dto.requests.CreateUserDTO;
import com.seb.eventpass.users.dto.requests.LoginRequestDTO;
import com.seb.eventpass.users.dto.responses.TokenResponseDTO;
import com.seb.eventpass.users.models.UserEventPass;
import com.seb.eventpass.users.models.UserRole;
import com.seb.eventpass.users.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReactiveAuthenticationManager authenticationManager;

    @Value("${SECRET_TOKEN_KEY}")
    private String SECRET_KEY;

    private final Long EXPIRATION_TIME = 864_000_000L;

    @Override
    public Mono<TokenResponseDTO> register(CreateUserDTO createUserDTO) {
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
                            .role(UserRole.ROLE_USER.name())
                            .build();
                    return userRepository.save(userEventPass)
                            .map(savedUser-> {
                                SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
                                String token = Jwts.builder()
                                        .subject(savedUser.getUuid().toString())
                                        .claim("email", savedUser.getEmail())
                                        .claim("roles", savedUser.getRole())
                                        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                                        .signWith(secretKey)
                                        .compact();
                                return new TokenResponseDTO(token);
                            });
                });
    }

    @Override
    public Mono<TokenResponseDTO> login(LoginRequestDTO loginRequestDTO) {
        UsernamePasswordAuthenticationToken unauthenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password());
        return authenticationManager.authenticate(unauthenticationToken)
                .flatMap(authResult -> {
                    return userRepository.findByEmail(loginRequestDTO.email())
                            .map(user -> {
                                String roles = authResult.getAuthorities().stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.joining(","));
                                SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

                                String token = Jwts.builder()
                                        .subject(user.getUuid().toString())
                                        .claim("email", user.getEmail())
                                        .claim("roles", roles)
                                        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                                        .signWith(key)
                                        .compact();
                                return new TokenResponseDTO(token);
                            });
                })
                .onErrorMap(e -> new IllegalStateException("Authentication failed: " + e.getMessage()));
    }

}

package com.seb.eventpass.users.security;

import com.seb.eventpass.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReactiveUserDetailsImpl implements ReactiveUserDetailsService {
    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return userRepository.findByEmail(email)
                .map(userEventPass -> {
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userEventPass.getRole());
                    return new User(userEventPass.getEmail(),
                            userEventPass.getPassword(),
                            true,
                            true,
                            true,
                            true,
                            List.of(simpleGrantedAuthority));
                });
    }
}

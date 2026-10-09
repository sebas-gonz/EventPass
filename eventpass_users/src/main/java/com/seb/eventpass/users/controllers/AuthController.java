package com.seb.eventpass.users.controllers;

import com.seb.eventpass.users.dto.requests.CreateUserDTO;
import com.seb.eventpass.users.dto.requests.LoginRequestDTO;
import com.seb.eventpass.users.dto.responses.TokenResponseDTO;
import com.seb.eventpass.users.models.UserEventPass;
import com.seb.eventpass.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserEventPass> register(@RequestBody Mono<CreateUserDTO> createUserDTOMono){
        return  createUserDTOMono
                .flatMap(userService::register);

    }

    @PostMapping("/login")
    public Mono<TokenResponseDTO> login(@RequestBody Mono<LoginRequestDTO> loginRequestDTOMono){
        return loginRequestDTOMono.flatMap(userService::login);
    }
}

package com.seb.eventpass.users.services;

import com.seb.eventpass.users.dto.requests.CreateUserDTO;
import com.seb.eventpass.users.dto.requests.LoginRequestDTO;
import com.seb.eventpass.users.dto.responses.TokenResponseDTO;
import com.seb.eventpass.users.models.UserEventPass;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserEventPass> register(CreateUserDTO createUserDTO);
    Mono<TokenResponseDTO> login(LoginRequestDTO loginRequestDTO);


}

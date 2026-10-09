package com.seb.eventpass.users.repositories;

import com.seb.eventpass.users.models.UserEventPass;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEventPass, Long> {
    Mono<UserEventPass> findByEmail(String email);
}

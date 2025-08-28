package com.crediya.autenticacion.r2dbc;

import com.crediya.autenticacion.model.user.User;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository
        extends ReactiveCrudRepository<UserData, String>,
        ReactiveQueryByExampleExecutor<UserData> {

    Mono<Boolean> existsByEmail(String email);
    Mono<UserData> findByEmail(String email);
    Mono<UserData> findById(UUID id);
}

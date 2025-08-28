package com.crediya.autenticacion.r2dbc;

import com.crediya.autenticacion.model.user.User;
import com.crediya.autenticacion.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@Slf4j
@Component
public class UserRepositoryAdapter
        extends ReactiveAdapterOperations<User, UserData, String, UserRepository>
        implements com.crediya.autenticacion.model.userrepository.UserRepository {

    public UserRepositoryAdapter(UserRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    @Transactional
    public Mono<User> save(User user) {
        log.info("[Adapter] Saving user email={}", user.getEmail());
        return super.save(user)
                .doOnSuccess(u -> log.info("[Adapter] Saved user id={}", u.getId()));
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email).map(d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> findById(UUID id) {
        return repository.findById(id).map(d -> mapper.map(d, User.class));
    }
}

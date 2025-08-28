package com.crediya.autenticacion;

import com.crediya.autenticacion.model.user.User;
import com.crediya.autenticacion.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserUseCase useCase;

    @Transactional
    public Mono<User> register(User user) {
        log.info("[AppService] Transactional register email={}", user.getEmail());
        return useCase.execute(user)
                .doOnSuccess(u -> log.info("[AppService] Transaction completed id={}", u.getId()));
    }
}
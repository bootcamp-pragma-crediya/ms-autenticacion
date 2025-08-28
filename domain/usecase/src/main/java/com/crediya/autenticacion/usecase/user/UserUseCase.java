package com.crediya.autenticacion.usecase.user;

import com.crediya.autenticacion.model.exceptions.DuplicateUserException;
import com.crediya.autenticacion.model.exceptions.InvalidUserException;
import com.crediya.autenticacion.model.user.User;
import com.crediya.autenticacion.model.userrepository.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository repository;

    public Mono<User> execute(User user) {

        if (user.getName() == null || user.getName().isBlank() ||
                user.getLastName() == null || user.getLastName().isBlank() ||
                user.getEmail() == null || user.getEmail().isBlank() ||
                user.getBaseSalary() == null) {
            return Mono.error(new InvalidUserException("Missing required fields"));
        }

        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(regex, user.getEmail())) {
            return Mono.error(new InvalidUserException("Invalid email format"));
        }

        if (user.getBaseSalary().compareTo(BigDecimal.ZERO) < 0 ||
                user.getBaseSalary().compareTo(new BigDecimal("15000000")) > 0) {
            return Mono.error(new InvalidUserException("Salary out of range"));
        }

        return repository.existsByEmail(user.getEmail())
                .flatMap(exists -> exists
                        ? Mono.<User>error(new DuplicateUserException("Email already exists"))
                        : repository.save(user));
    }
}
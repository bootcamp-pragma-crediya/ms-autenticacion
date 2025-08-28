package com.crediya.autenticacion.usecase.user;

import com.crediya.autenticacion.model.exceptions.DuplicateUserException;
import com.crediya.autenticacion.model.exceptions.InvalidUserException;
import com.crediya.autenticacion.model.user.User;
import com.crediya.autenticacion.model.userrepository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserUseCasePlainMockitoTest {

    private UserRepository repository;
    private UserUseCase useCase;
    private User valid;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);      // <-- mock manual
        useCase = new UserUseCase(repository);        // <-- inyectas tú misma

        valid = User.builder()
                .name("Jane")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990,1,1))
                .address("Street 1")
                .phone("123")
                .email("jane@doe.com")
                .baseSalary(new BigDecimal("1000000"))
                .build();
    }

    @Test
    void createsUserWhenValidAndNotExists() {
        when(repository.existsByEmail("jane@doe.com")).thenReturn(Mono.just(false));
        when(repository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0, User.class);
            return Mono.just(u.toBuilder().id(UUID.randomUUID()).build());
        });

        StepVerifier.create(useCase.execute(valid))
                .expectNextMatches(u -> u.getId()!=null && u.getEmail().equals("jane@doe.com"))
                .verifyComplete();

        verify(repository).existsByEmail("jane@doe.com");
        verify(repository).save(any(User.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void failsWhenRequiredFieldsMissing() {
        User missing = valid.toBuilder().name(null).build();

        StepVerifier.create(useCase.execute(missing))
                .expectError(InvalidUserException.class)
                .verify();

        verifyNoInteractions(repository);
    }

    @Test
    void failsWhenEmailInvalid() {
        User invalid = valid.toBuilder().email("bad-email").build();
        StepVerifier.create(useCase.execute(invalid))
                .expectError(InvalidUserException.class)
                .verify();

        verifyNoInteractions(repository);
    }

    @Test
    void failsWhenSalaryTooLow() {
        User invalid = valid.toBuilder().baseSalary(new BigDecimal("-1")).build();
        StepVerifier.create(useCase.execute(invalid))
                .expectError(InvalidUserException.class)
                .verify();

        verifyNoInteractions(repository);
    }

    @Test
    void failsWhenSalaryTooHigh() {
        User invalid = valid.toBuilder().baseSalary(new BigDecimal("15000001")).build();
        StepVerifier.create(useCase.execute(invalid))
                .expectError(InvalidUserException.class)
                .verify();

        verifyNoInteractions(repository);
    }

    @Test
    void failsWhenEmailAlreadyExists() {
        when(repository.existsByEmail("jane@doe.com")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.execute(valid))
                .expectError(DuplicateUserException.class)
                .verify();

        verify(repository).existsByEmail("jane@doe.com");
        verify(repository, never()).save(any());
        verifyNoMoreInteractions(repository);
    }
}
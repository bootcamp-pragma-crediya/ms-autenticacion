package com.crediya.autenticacion.api;

import com.crediya.autenticacion.api.dto.UserRequest;
import com.crediya.autenticacion.api.dto.UserResponse;
import com.crediya.autenticacion.api.mapper.UserDtoMapperImpl;
import com.crediya.autenticacion.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(
        controllers = UserController.class,
        excludeAutoConfiguration = {
                R2dbcAutoConfiguration.class,
                R2dbcDataAutoConfiguration.class
        }
)
@Import({UserDtoMapperImpl.class, GlobalExceptionHandler.class})
@TestPropertySource(properties = {
        "springdoc.enabled=false",
        "springdoc.api-docs.enabled=false"
})
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @TestConfiguration
    static class Mocks {
        @Bean
        UserUseCase userUseCase() {
            return Mockito.mock(UserUseCase.class);
        }
    }

    @Autowired
    UserUseCase userUseCase;

    @Test
    void shouldCreateUser() {
        var saved = com.crediya.autenticacion.model.user.User.builder()
                .id(UUID.randomUUID())
                .name("Jane").lastName("Doe")
                .dateOfBirth(LocalDate.of(1990,1,1))
                .address("Street 1").phone("123")
                .email("jane@doe.com")
                .baseSalary(new BigDecimal("1000000"))
                .build();

        Mockito.when(userUseCase.execute(any())).thenReturn(Mono.just(saved));

        var req = new UserRequest(
                "Jane","Doe", LocalDate.of(1990,1,1),
                "Street 1","123","jane@doe.com", new BigDecimal("1000000"));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location")
                .expectBody(UserResponse.class)
                .value(r -> {
                    assert r.id() != null;
                    assert r.email().equals("jane@doe.com");
                });
    }
}

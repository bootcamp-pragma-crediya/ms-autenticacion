package com.crediya.autenticacion.api;
import com.crediya.autenticacion.api.dto.UserRequest;
import com.crediya.autenticacion.api.dto.UserResponse;
import com.crediya.autenticacion.api.mapper.UserDtoMapper;
import com.crediya.autenticacion.usecase.user.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
@Slf4j
@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User registration")
public class UserController {

    private final UserUseCase userUseCase;
    private final UserDtoMapper mapper;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user with personal information"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully created",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User with the given email already exists"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @PostMapping
    public Mono<ResponseEntity<UserResponse>> register(@Valid @RequestBody UserRequest request) {
        log.info("[Controller] POST /api/v1/usuarios email={}", request.email());
        return userUseCase.execute(mapper.toDomain(request))
                .map(mapper::toResponse)
                .doOnSuccess(resp -> log.info("[Controller] Created id={} email={}", resp.id(), resp.email()))
                .map(resp -> ResponseEntity.created(URI.create("/api/v1/usuarios/" + resp.id())).body(resp));
    }
}
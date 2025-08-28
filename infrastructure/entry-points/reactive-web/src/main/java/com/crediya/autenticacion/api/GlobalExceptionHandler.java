package com.crediya.autenticacion.api;


import com.crediya.autenticacion.model.exceptions.DuplicateUserException;
import com.crediya.autenticacion.model.exceptions.InvalidUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.crediya.autenticacion.api")
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Map<String, Object>> handleBeanValidation(WebExchangeBindException ex) {
        log.warn("[Error] Bean validation: {}", ex.getMessage());
        return Mono.just(Map.of("message", "Validación fallida"));
    }

    @ExceptionHandler(ServerWebInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Map<String, Object>> handleBadJson(ServerWebInputException ex) {
        log.warn("[Error] Bad input: {}", ex.getReason());
        return Mono.just(Map.of("message", "Entrada inválida"));
    }

    @ExceptionHandler(InvalidUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Map<String, Object>> handleInvalid(InvalidUserException ex) {
        log.warn("[Error] {}", ex.getMessage());
        return Mono.just(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler({DuplicateUserException.class, DuplicateKeyException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<Map<String, Object>> handleDuplicate(RuntimeException ex) {
        log.warn("[Error] {}", ex.getMessage());
        return Mono.just(Map.of("message", "El correo ya está registrado"));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<Map<String, Object>> handleAny(Throwable ex) {
        log.error("[Error] Unexpected", ex);
        return Mono.just(Map.of("message", "Ocurrió un error. Intenta más tarde."));
    }
}
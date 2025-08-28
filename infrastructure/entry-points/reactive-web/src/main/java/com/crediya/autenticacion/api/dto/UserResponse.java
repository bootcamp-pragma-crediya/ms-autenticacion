package com.crediya.autenticacion.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserResponse(
        @JsonProperty("id") UUID id,
        @JsonProperty("name") String name,
        @JsonProperty("last_name") String lastName,
        @JsonProperty("date_of_birth") LocalDate dateOfBirth,
        @JsonProperty("address") String address,
        @JsonProperty("phone") String phone,
        @JsonProperty("email") String email,
        @JsonProperty("base_salary") BigDecimal baseSalary
) {}

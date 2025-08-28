package com.crediya.autenticacion.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;


import java.math.BigDecimal;
import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserRequest(
        @JsonProperty("name") @NotBlank String name,
        @JsonProperty("last_name") @NotBlank String lastName,
        @JsonProperty("date_of_birth") LocalDate dateOfBirth,
        @JsonProperty("address") String address,
        @JsonProperty("phone") String phone,
        @JsonProperty("email") @NotBlank @Email String email,
        @JsonProperty("base_salary") @NotNull @DecimalMin("0.0") @DecimalMax("15000000.0") BigDecimal baseSalary
) {}
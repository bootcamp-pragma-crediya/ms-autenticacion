package com.crediya.autenticacion.model.user;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private UUID id;
    private String name;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private String phone;
    private String email;
    private BigDecimal baseSalary;
}

package com.crediya.autenticacion.r2dbc;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
@Builder(toBuilder = true)
public class UserData {
    @Id
    private UUID id;
    private String name;
    @Column("last_name")
    private String lastName;
    @Column("date_of_birth")
    private LocalDate dateOfBirth;
    private String address;
    private String phone;
    private String email;
    @Column("base_salary")
    private BigDecimal baseSalary;
}

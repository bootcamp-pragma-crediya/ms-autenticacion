package com.crediya.autenticacion.config;

import com.crediya.autenticacion.model.userrepository.UserRepository;
import com.crediya.autenticacion.usecase.user.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.crediya.autenticacion.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCaseConfig {

    @Bean
    public UserUseCase userUseCase(UserRepository repository) {
        return new UserUseCase(repository);
    }
}
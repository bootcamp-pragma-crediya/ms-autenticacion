package com.crediya.autenticacion.api.mapper;

import com.crediya.autenticacion.api.dto.UserRequest;
import com.crediya.autenticacion.api.dto.UserResponse;
import com.crediya.autenticacion.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    User toDomain(UserRequest request);
    UserResponse toResponse(User user);
}
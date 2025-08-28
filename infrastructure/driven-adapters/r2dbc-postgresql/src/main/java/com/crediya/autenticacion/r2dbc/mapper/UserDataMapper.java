package com.crediya.autenticacion.r2dbc.mapper;

import com.crediya.autenticacion.model.user.User;
import com.crediya.autenticacion.r2dbc.UserData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDataMapper {
    UserData toData(User domain);
    User toDomain(UserData data);
}
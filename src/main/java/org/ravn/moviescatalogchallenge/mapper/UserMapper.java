package org.ravn.moviescatalogchallenge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.ravn.moviescatalogchallenge.aggregate.request.UserRequest;
import org.ravn.moviescatalogchallenge.aggregate.request.UserRequestUpdate;
import org.ravn.moviescatalogchallenge.aggregate.response.UserResponse;
import org.ravn.moviescatalogchallenge.entity.Role;
import org.ravn.moviescatalogchallenge.entity.UserEntity;

import java.sql.Date;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "userRequest.email", target = "email")
    @Mapping(source = "userRequest.password", target = "password")
    @Mapping(source = "role", target = "role")
    UserEntity userRequestToUser(UserRequest userRequest, Role role);

    @Mapping(source = "email", target = "email")
    @Mapping(source = "role.role", target = "role")
    UserResponse userToUserResponse(UserEntity userEntity);

    @Mapping(source = "userRequestUpdate.password", target = "password")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "userEntity.email", target = "email")
    @Mapping(source = "userEntity.userId", target = "userId")
    UserEntity userRequestUpdateToUser(UserRequestUpdate userRequestUpdate, Role role, UserEntity userEntity);
}

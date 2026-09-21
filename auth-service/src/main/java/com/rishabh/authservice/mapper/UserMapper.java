package com.rishabh.authservice.mapper;

import com.rishabh.authservice.dto.request.UserRequestDto;
import com.rishabh.authservice.dto.response.UserResponseDto;
import com.rishabh.authservice.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserRequestDto requestDto);

    UserResponseDto toResponseDto(User user);
}

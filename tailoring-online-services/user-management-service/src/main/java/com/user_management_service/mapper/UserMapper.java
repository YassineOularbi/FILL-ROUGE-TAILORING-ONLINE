package com.user_management_service.mapper;

import com.user_management_service.dto.UserDto;
import com.user_management_service.model.Admin;
import com.user_management_service.model.Customer;
import com.user_management_service.model.Tailor;
import com.user_management_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toEntity(UserDto userDto);
    UserDto toDto(User user);
    List<User> toEntities(List<UserDto> userDtoList);
    List<UserDto> toDtoList(List<User> users);
    User partialUpdate(UserDto userDto, @MappingTarget User user);
}

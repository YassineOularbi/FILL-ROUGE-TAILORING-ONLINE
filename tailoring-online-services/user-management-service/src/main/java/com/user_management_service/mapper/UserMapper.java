package com.user_management_service.mapper;

import com.user_management_service.dto.UserDto;
import com.user_management_service.model.Tailor;
import com.user_management_service.model.User;
import jakarta.persistence.MappedSuperclass;
import org.mapstruct.*;
@MappedSuperclass
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toEntity(UserDto userDto);
    UserDto toDto(User user);
    User partialUpdate(UserDto userDto, @MappingTarget User user);
}

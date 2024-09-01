package com.user_management_service.mapper;

import com.user_management_service.dto.AdminDto;
import com.user_management_service.model.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminMapper {
    Admin toEntity(AdminDto adminDto);
    AdminDto toDto(Admin admin);
    List<Admin> toEntities(List<AdminDto> adminDtoList);
    List<AdminDto> toDtoList(List<Admin> admins);
    Admin partialUpdate(AdminDto adminDto, @MappingTarget Admin admin);
}

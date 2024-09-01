package com.user_management_service.mapper;

import com.user_management_service.dto.TailorDto;
import com.user_management_service.model.Tailor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TailorMapper {
    Tailor toEntity(TailorDto tailorDto);
    TailorDto toDto(Tailor tailor);
    List<Tailor> toEntities(List<TailorDto> tailorDtoList);
    List<TailorDto> toDtoList(List<Tailor> tailors);
    Tailor partialUpdate(TailorDto tailorDto, @MappingTarget Tailor tailor);
}

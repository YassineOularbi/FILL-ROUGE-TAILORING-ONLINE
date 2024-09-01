package com.user_management_service.mapper;

import com.user_management_service.dto.BankDto;
import com.user_management_service.model.Bank;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BankMapper {
    Bank toEntity(BankDto bankDto);
    BankDto toDto(Bank bank);
    List<Bank> toEntities(List<BankDto> bankDtoList);
    List<BankDto> toDtoList(List<Bank> banks);
}

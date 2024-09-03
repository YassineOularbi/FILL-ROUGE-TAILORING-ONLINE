//package com.user_management_service.mapper;
//
//import com.user_management_service.dto.BankDto;
//import com.user_management_service.model.Bank;
//import org.mapstruct.Mapper;
//import org.mapstruct.MappingTarget;
//import org.mapstruct.ReportingPolicy;
//
//@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
//public interface BankMapper {
//    Bank toEntity(BankDto bankDto);
//    BankDto toDto(Bank bank);
//    Bank partialUpdate(BankDto bankDto, @MappingTarget Bank bank);
//}

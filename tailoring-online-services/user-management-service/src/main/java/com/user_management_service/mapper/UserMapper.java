//package com.user_management_service.mapper;
//
//import com.user_management_service.dto.*;
//import com.user_management_service.model.*;
//import org.mapstruct.*;
//
//@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
//public interface UserMapper {
//
//    @SubclassMapping(source = AdminDto.class, target = Admin.class)
//    @SubclassMapping(source = TailorDto.class, target = Tailor.class)
//    @SubclassMapping(source = CustomerDto.class, target = Customer.class)
//    User toEntity(UserDto userDto);
//    @SubclassMapping(source = Admin.class, target = AdminDto.class)
//    @SubclassMapping(source = Tailor.class, target = TailorDto.class)
//    @SubclassMapping(source = Customer.class, target = CustomerDto.class)
//    UserDto toDto(User user);
//
//    User partialUpdate(UserDto userDto, @MappingTarget User user);
//
//}

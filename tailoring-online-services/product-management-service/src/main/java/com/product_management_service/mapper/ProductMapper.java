package com.product_management_service.mapper;

import com.product_management_service.dto.ProductDto;
import com.product_management_service.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    Product toEntity(ProductDto productDto);
    ProductDto toDto(Product product);
    Product partialUpdate(ProductDto productDto, @MappingTarget Product product);
}

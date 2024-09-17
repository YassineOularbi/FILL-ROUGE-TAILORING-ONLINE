package com.order_management_service.service;

import com.order_management_service.client.StoreManagementClient;
import com.order_management_service.dto.CustomizedProductDto;
import com.order_management_service.exception.*;
import com.order_management_service.mapper.CustomizedProductMapper;
import com.order_management_service.model.CustomizedProduct;
import com.order_management_service.repository.CustomizedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomizedProductService {

    private final CustomizedProductRepository customizedProductRepository;
    private final CustomizedProductMapper customizedProductMapper;
    private final StoreManagementClient storeManagementClient;

    public List<CustomizedProduct> getAllCustomizedProducts() {
        return customizedProductRepository.findAll();
    }

    public CustomizedProduct getCustomizedProductById(Long id) {
        return customizedProductRepository.findById(id)
                .orElseThrow(() -> new CustomizedProductNotFoundException(id));
    }

    public CustomizedProductDto getCustomizedProductWithProduct(Long id){
        var customizedProduct = customizedProductRepository.findById(id).orElseThrow(() -> new CustomizedProductNotFoundException(id));
        var product = storeManagementClient.getProductById(String.valueOf(customizedProduct.getProductId())).orElseThrow(() -> new ProductNotFoundException(customizedProduct.getProductId()));
        var mappedCustomizedProduct = customizedProductMapper.toDto(customizedProduct);
        mappedCustomizedProduct.setProduct(product);
        return mappedCustomizedProduct;
    }

    public CustomizedProductDto addCustomizedProduct(CustomizedProductDto customizedProductDto, Long productId) {
        var product = storeManagementClient.getProductById(String.valueOf(productId)).orElseThrow(() -> new ProductNotFoundException(productId));
        var mappedCustomizedProduct = customizedProductMapper.toEntity(customizedProductDto);
        mappedCustomizedProduct.setProductId(product.getId());
        var savedCustomizedProduct = customizedProductRepository.save(mappedCustomizedProduct);
        return customizedProductMapper.toDto(savedCustomizedProduct);
    }

    public CustomizedProductDto updateCustomizedProduct(Long id, CustomizedProductDto customizedProductDto) {
        var existingCustomizedProduct = customizedProductRepository.findById(id).orElseThrow(() -> new CustomizedProductNotFoundException(id));
        var updatedCustomizedProduct = customizedProductMapper.partialUpdate(customizedProductDto, existingCustomizedProduct);
        var savedCustomizedProduct = customizedProductRepository.save(updatedCustomizedProduct);
        return customizedProductMapper.toDto(savedCustomizedProduct);
    }

    public void deleteCustomizedProduct(Long id) {
        var customizedProduct = customizedProductRepository.findById(id).orElseThrow(() -> new CustomizedProductNotFoundException(id));
        customizedProductRepository.delete(customizedProduct);
    }
}

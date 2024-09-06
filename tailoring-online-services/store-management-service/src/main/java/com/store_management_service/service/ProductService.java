package com.store_management_service.service;

import com.store_management_service.dto.ProductDto;
import com.store_management_service.exception.ProductNotFoundException;
import com.store_management_service.exception.StoreNotFoundException;
import com.store_management_service.mapper.ProductMapper;
import com.store_management_service.model.Product;
import com.store_management_service.repository.ProductRepository;
import com.store_management_service.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final StoreRepository storeRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public ProductDto addProduct(ProductDto productDto, Long storeId) {
        var store = storeRepository.findById(storeId).orElseThrow(() -> new StoreNotFoundException(storeId));
        var mappedProduct = productMapper.toEntity(productDto);
        mappedProduct.setStore(store);
        String sku = STR."TO-\{mappedProduct.getCategory()}-MA-\{(int) (Math.random() * 10000)}";
        mappedProduct.setCodeSKU(sku);
        var savedProduct = productRepository.save(mappedProduct);
        return productMapper.toDto(savedProduct);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {
        var existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        var updatedProduct = productMapper.partialUpdate(productDto, existingProduct);
        var savedProduct = productRepository.save(updatedProduct);
        return productMapper.toDto(savedProduct);
    }

    public void deleteProduct(Long id) {
        var product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }

}
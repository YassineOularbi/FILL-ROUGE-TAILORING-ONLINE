package com.product_management_service.service;

import com.product_management_service.client.StoreManagementClient;
import com.product_management_service.dto.ProductDto;
import com.product_management_service.exception.ProductNotFoundException;
import com.product_management_service.exception.StoreNotFoundException;
import com.product_management_service.mapper.ProductMapper;
import com.product_management_service.model.Product;
import com.product_management_service.repository.ProductRepository;
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
    private final StoreManagementClient storeManagementClient;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public ProductDto getProductWithStore(Long id) {
        var product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        var store = storeManagementClient.getStoreById(product.getStoreId().toString()).orElseThrow(() -> new StoreNotFoundException(product.getStoreId()));
        var mappedProduct = productMapper.toDto(product);
        mappedProduct.setStore(store);
        return mappedProduct;
    }

    public ProductDto addProduct(ProductDto productDto, Long storeId) {
        var supplier = storeManagementClient.getStoreById(storeId.toString()).orElseThrow(() -> new StoreNotFoundException(storeId));
        var existingProduct = productRepository.findByStoreId(storeId);
        if (existingProduct.isPresent()) {
            return updateProduct(existingProduct.get().getId(), productDto);
        } else {
            var mappedProduct = productMapper.toEntity(productDto);
            mappedProduct.setStoreId(supplier.getId());
            String sku = STR."TO-\{mappedProduct.getCategory()}-MA-\{(int) (Math.random() * 10000)}";
            mappedProduct.setCodeSKU(sku);
            var savedProduct = productRepository.save(mappedProduct);
            return productMapper.toDto(savedProduct);
        }
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
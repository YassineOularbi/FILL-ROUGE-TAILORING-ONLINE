package com.store_management_service.service.jpa;

import com.store_management_service.dto.ProductDto;
import com.store_management_service.exception.ProductNotFoundException;
import com.store_management_service.exception.StoreNotFoundException;
import com.store_management_service.mapper.ProductMapper;
import com.store_management_service.model.Product;
import com.store_management_service.repository.jpa.ProductJpaRepository;
import com.store_management_service.repository.jpa.StoreJpaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductJpaService {

    private static final Logger logger = LoggerFactory.getLogger(ProductJpaService.class);
    private final ProductJpaRepository productRepository;
    private final ProductMapper productMapper;
    private final StoreJpaRepository storeRepository;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<ProductDto> getAllProducts(int page, int size) {
        logger.info("Fetching all products with page: {}, size: {}", page, size);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        logger.info("Fetched {} products", productPage.getTotalElements());

        List<ProductDto> productDtos = productPage.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(productDtos, pageable, productPage.getTotalElements());
    }

    public List<Product> getAllProductsByStore(Long id) {
        return productRepository.getAllByStoreId(id);
    }

    public Product getProductById(Long id) {
        logger.info("Fetching product with ID: {}", id);
        return productRepository.findById(id).orElseThrow(() -> {
            logger.error("Product not found with ID: {}", id);
            return new ProductNotFoundException(id);
        });
    }

    public ProductDto addProduct(ProductDto productDto, Long storeId) {
        logger.info("Adding new product");
        var store = storeRepository.findById(storeId).orElseThrow(() -> {
            logger.error("Store not found with ID: {}", storeId);
            return new StoreNotFoundException(storeId);
        });
        var mappedProduct = productMapper.toEntity(productDto);
        mappedProduct.setStore(store);
        String sku = String.format("TO-%s-MA-%d", mappedProduct.getCategory(), (int) (Math.random() * 10000));
        mappedProduct.setCodeSKU(sku);
        var savedProduct = productRepository.save(mappedProduct);
        return productMapper.toDto(savedProduct);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {
        logger.info("Updating product with ID: {}", id);
        var existingProduct = productRepository.findById(id).orElseThrow(() -> {
            logger.error("Product not found with ID: {}", id);
            return new ProductNotFoundException(id);
        });
        var updatedProduct = productMapper.partialUpdate(productDto, existingProduct);
        var savedProduct = productRepository.save(updatedProduct);
        return productMapper.toDto(savedProduct);
    }

    public void deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);
        var product = productRepository.findById(id).orElseThrow(() -> {
            logger.error("Product not found with ID: {}", id);
            return new ProductNotFoundException(id);
        });
        productRepository.delete(product);
    }
}

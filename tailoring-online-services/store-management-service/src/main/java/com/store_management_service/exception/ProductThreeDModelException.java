package com.store_management_service.exception;

public class ProductThreeDModelException extends RuntimeException {
    public ProductThreeDModelException(Long productId, Long threeDModelId){
        super(String.format("Product with id: %s already has a 3D Model with id: %s", productId, threeDModelId));
    }

    public ProductThreeDModelException(Long productId) {
        super(String.format("No 3D Model found for product with id: %s", productId));
    }
}

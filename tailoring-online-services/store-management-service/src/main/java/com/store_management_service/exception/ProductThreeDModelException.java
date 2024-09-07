package com.store_management_service.exception;

public class ProductThreeDModelException extends RuntimeException {
    public ProductThreeDModelException(Long productId, Long threeDModelId){
        super(STR."Product with id : \{productId} Already has a 3D Model with id : \{threeDModelId}");
    }

    public ProductThreeDModelException(Long productId) {
        super(STR."No 3D Model found for product with id : \{productId}");
    }
}

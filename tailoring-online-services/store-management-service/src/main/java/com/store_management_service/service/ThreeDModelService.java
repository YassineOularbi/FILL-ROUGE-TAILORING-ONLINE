package com.store_management_service.service;

import com.store_management_service.dto.ThreeDModelDto;
import com.store_management_service.exception.ProductNotFoundException;
import com.store_management_service.exception.ProductThreeDModelException;
import com.store_management_service.exception.ThreeDModelNotFoundException;
import com.store_management_service.mapper.ThreeDModelMapper;
import com.store_management_service.model.ThreeDModel;
import com.store_management_service.repository.ProductRepository;
import com.store_management_service.repository.ThreeDModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ThreeDModelService {

    private final ThreeDModelRepository threeDModelRepository;
    private final ThreeDModelMapper threeDModelMapper;
    private final ProductRepository productRepository;

    public List<ThreeDModelDto> getAllThreeDModel(){
        return threeDModelMapper.toDtos(threeDModelRepository.findAll());
    }

    public ThreeDModel getThreeDModelByProduct(Long id){
        var product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        return threeDModelRepository.getByProductId(product.getId()).orElseThrow(() -> new ProductThreeDModelException(product.getId()));
    }

    public ThreeDModel getThreeDModelById(Long id){
        return threeDModelRepository.findById(id).orElseThrow(() -> new  ThreeDModelNotFoundException(id));
    }

    public ThreeDModelDto addThreeDModel(Long productId){
        var product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        if (product.getThreeDModel() == null){
            var threeDModel = new ThreeDModel();
            threeDModel.setProduct(product);
            var savedThreeDModel = threeDModelRepository.save(threeDModel);
            product.setThreeDModel(savedThreeDModel);
            productRepository.save(product);
            return threeDModelMapper.toDto(savedThreeDModel);
        } else {
           throw new ProductThreeDModelException(product.getId(), product.getThreeDModel().getId());
        }
    }

    public void deleteThreeDModel(Long id) {
        var existingThreeDModel = threeDModelRepository.findById(id).orElseThrow(() -> new ThreeDModelNotFoundException(id));
        threeDModelRepository.delete(existingThreeDModel);
    }

}

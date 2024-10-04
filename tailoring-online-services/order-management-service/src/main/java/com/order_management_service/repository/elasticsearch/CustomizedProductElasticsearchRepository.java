package com.order_management_service.repository.elasticsearch;

import com.order_management_service.model.CustomizedProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CustomizedProductElasticsearchRepository extends ElasticsearchRepository<CustomizedProduct, Long> {
}

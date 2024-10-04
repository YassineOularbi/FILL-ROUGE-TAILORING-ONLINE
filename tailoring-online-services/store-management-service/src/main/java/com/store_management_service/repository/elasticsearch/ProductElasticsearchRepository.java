package com.store_management_service.repository.elasticsearch;

import com.store_management_service.model.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductElasticsearchRepository extends ElasticsearchRepository<Product, Long> {
}

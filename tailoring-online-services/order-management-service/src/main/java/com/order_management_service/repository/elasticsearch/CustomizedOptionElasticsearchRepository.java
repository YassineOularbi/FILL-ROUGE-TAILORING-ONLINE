package com.order_management_service.repository.elasticsearch;

import com.order_management_service.model.CustomizedOption;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CustomizedOptionElasticsearchRepository extends ElasticsearchRepository<CustomizedOption, Long> {
}

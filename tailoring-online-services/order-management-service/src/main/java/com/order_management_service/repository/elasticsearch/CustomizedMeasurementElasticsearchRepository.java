package com.order_management_service.repository.elasticsearch;

import com.order_management_service.model.CustomizedMeasurement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CustomizedMeasurementElasticsearchRepository extends ElasticsearchRepository<CustomizedMeasurement, Long> {
}

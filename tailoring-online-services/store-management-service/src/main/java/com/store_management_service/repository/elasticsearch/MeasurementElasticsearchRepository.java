package com.store_management_service.repository.elasticsearch;

import com.store_management_service.model.Measurement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MeasurementElasticsearchRepository extends ElasticsearchRepository<Measurement, Long> {
}

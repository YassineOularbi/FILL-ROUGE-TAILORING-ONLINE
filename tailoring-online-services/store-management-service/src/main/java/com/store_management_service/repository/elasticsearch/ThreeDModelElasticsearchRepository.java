package com.store_management_service.repository.elasticsearch;

import com.store_management_service.model.ThreeDModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ThreeDModelElasticsearchRepository extends ElasticsearchRepository<ThreeDModel, Long> {
}

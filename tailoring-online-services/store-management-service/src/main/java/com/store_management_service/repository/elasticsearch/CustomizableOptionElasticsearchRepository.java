package com.store_management_service.repository.elasticsearch;

import com.store_management_service.model.CustomizableOption;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CustomizableOptionElasticsearchRepository extends ElasticsearchRepository<CustomizableOption, Long> {
}

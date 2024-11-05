package com.store_management_service.repository.elasticsearch;

import com.store_management_service.model.Material;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MaterialElasticsearchRepository extends ElasticsearchRepository<Material, Long> {
}

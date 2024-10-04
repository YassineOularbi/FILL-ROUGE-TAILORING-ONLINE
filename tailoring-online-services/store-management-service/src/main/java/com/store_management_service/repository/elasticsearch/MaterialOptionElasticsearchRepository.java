package com.store_management_service.repository.elasticsearch;

import com.store_management_service.model.MaterialOption;
import com.store_management_service.model.MaterialOptionKey;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MaterialOptionElasticsearchRepository extends ElasticsearchRepository<MaterialOption, MaterialOptionKey> {
}

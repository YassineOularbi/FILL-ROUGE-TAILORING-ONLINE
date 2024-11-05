package com.store_management_service.repository.elasticsearch;

import com.store_management_service.model.Store;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StoreElasticsearchRepository extends ElasticsearchRepository<Store, Long> {
}

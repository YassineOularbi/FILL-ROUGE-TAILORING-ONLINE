package com.localization_shipping_service.repository.elasticsearch;

import com.localization_shipping_service.model.Address;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AddressElasticsearchRepository extends ElasticsearchRepository<Address, Long> {
}

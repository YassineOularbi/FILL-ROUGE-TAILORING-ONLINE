package com.payment_banking_service.repository.elasticsearch;

import com.payment_banking_service.model.Bank;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BankElasticsearchRepository extends ElasticsearchRepository<Bank, Long> {
}

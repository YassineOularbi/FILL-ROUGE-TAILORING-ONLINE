package com.payment_banking_service.service.elasticsearch;

import co.elastic.clients.json.JsonData;
import com.payment_banking_service.model.Bank;
import com.payment_banking_service.repository.elasticsearch.BankElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankElasticsearchService {

    private static final Logger logger = LoggerFactory.getLogger(BankElasticsearchService.class);
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final BankElasticsearchRepository bankElasticsearchRepository;
    private static final int MAX_PAGE_SIZE = 100;

    @PostPersist
    @PostUpdate
    public void handleAfterCreateOrUpdate(Bank bank) {
        logger.info("Saving bank: {}", bank);
        bankElasticsearchRepository.save(bank);
    }

    @PostRemove
    public void handleAfterDelete(Bank bank) {
        logger.info("Deleting bank: {}", bank);
        bankElasticsearchRepository.delete(bank);
    }

    public Page<Bank> search(String input, int page, int size, String sortField, String sortDirection) {
        logger.info("Searching for banks with input: {}, page: {}, size: {}, sortField: {}, sortDirection: {}", input, page, size, sortField, sortDirection);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .should(s -> s
                                        .multiMatch(mm -> mm
                                                .query(input)
                                                .fields("cardNumber^3", "expirationDate", "cvc", "userId")
                                                .fuzziness("AUTO")
                                                .prefixLength(2)
                                        )
                                )
                        )
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<Bank> searchHits = elasticsearchTemplate.search(query, Bank.class);
        List<Bank> banks = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (banks.isEmpty()) {
            logger.warn("No banks found for input: {}", input);
        } else {
            logger.info("Found {} banks", banks.size());
        }

        return new PageImpl<>(banks, pageable, searchHits.getTotalHits());
    }

    public Page<Bank> filter(int page, int size, String sortField, String sortDirection, String cardNumberFilter, String expirationDateFrom, String expirationDateTo, String cvcFilter, String userIdFilter) {
        logger.info("Filtering banks with filters: cardNumberFilter={}, expirationDateFrom={}, expirationDateTo={}, cvcFilter={}, userIdFilter={}", cardNumberFilter, expirationDateFrom, expirationDateTo, cvcFilter, userIdFilter);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            if (cardNumberFilter != null && !cardNumberFilter.isEmpty()) {
                                b.should(s -> s.multiMatch(mm -> mm
                                        .query(cardNumberFilter.toLowerCase())
                                        .fields("cardNumber")
                                        .fuzziness("AUTO").prefixLength(2)
                                ));
                            }
                            if (expirationDateFrom != null || expirationDateTo != null) {
                                b.filter(f -> f.range(r -> {
                                    if (expirationDateFrom != null) {
                                        r.field("expirationDate").gte(JsonData.fromJson(expirationDateFrom));
                                    }
                                    if (expirationDateTo != null) {
                                        r.field("expirationDate").lte(JsonData.fromJson(expirationDateTo));
                                    }
                                    return r;
                                }));
                            }
                            if (cvcFilter != null && !cvcFilter.isEmpty()) {
                                b.filter(f -> f.term(t -> t.field("cvc").value(cvcFilter)));
                            }
                            if (userIdFilter != null && !userIdFilter.isEmpty()) {
                                b.filter(f -> f.term(t -> t.field("userId").value(userIdFilter.toLowerCase())));
                            }
                            return b;
                        })
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<Bank> searchHits = elasticsearchTemplate.search(queryBuilder, Bank.class);
        List<Bank> banks = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (banks.isEmpty()) {
            logger.warn("No banks found after filtering");
        } else {
            logger.info("Found {} banks after filtering", banks.size());
        }

        return new PageImpl<>(banks, pageable, searchHits.getTotalHits());
    }

    public List<String> autocomplete(String input) {
        logger.info("Generating autocomplete suggestions for input: {}", input);

        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            b.should(s -> s.multiMatch(mm -> mm
                                    .query(input.toLowerCase())
                                    .fields("cardNumber", "expirationDate", "cvc", "userId")
                                    .fuzziness("AUTO")
                                    .prefixLength(1)
                            ));
                            return b;
                        })
                )
                .build();

        SearchHits<Bank> searchHits = elasticsearchTemplate.search(queryBuilder, Bank.class);
        List<String> suggestions = new ArrayList<>();
        for (SearchHit<Bank> hit : searchHits.getSearchHits()) {
            Bank bank = hit.getContent();
            suggestions.add(bank.getCardNumber());
            suggestions.add(bank.getExpirationDate().toString());
            suggestions.add(bank.getCvc());
            suggestions.add(bank.getUserId());
        }

        if (suggestions.isEmpty()) {
            logger.warn("No autocomplete suggestions found for input: {}", input);
        } else {
            logger.info("Found {} autocomplete suggestions", suggestions.size());
        }

        return suggestions.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }
}

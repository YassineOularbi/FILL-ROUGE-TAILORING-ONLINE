package com.store_management_service.service.elasticsearch;

import com.store_management_service.model.Store;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreElasticsearchService {

    private static final Logger logger = LoggerFactory.getLogger(StoreElasticsearchService.class);
    private final ElasticsearchTemplate elasticsearchTemplate;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<Store> search(String input, int page, int size, String sortField, String sortDirection) {
        logger.info("Searching for stores with input: {}, page: {}, size: {}, sortField: {}, sortDirection: {}", input, page, size, sortField, sortDirection);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size);

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .should(s -> s
                                        .multiMatch(mm -> mm
                                                .query(input)
                                                .fields("name^3", "description", "tailorId")
                                                .fuzziness("AUTO")
                                                .prefixLength(2)
                                        )
                                )
                        )
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<Store> searchHits = elasticsearchTemplate.search(query, Store.class);
        List<Store> stores = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (stores.isEmpty()) {
            logger.warn("No stores found for input: {}", input);
        } else {
            logger.info("Found {} stores", stores.size());
        }

        return new PageImpl<>(stores, pageable, searchHits.getTotalHits());
    }

    public Page<Store> filter(int page, int size, String sortField, String sortDirection, String nameFilter) {
        logger.info("Filtering stores with filters: nameFilter={}", nameFilter);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size);

        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            if (nameFilter != null && !nameFilter.isEmpty()) {
                                b.should(s -> s.multiMatch(mm -> mm
                                        .query(nameFilter.toLowerCase())
                                        .fields("name")
                                        .fuzziness("AUTO")
                                        .prefixLength(2)
                                ));
                            }
                            return b;
                        })
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<Store> searchHits = elasticsearchTemplate.search(queryBuilder, Store.class);
        List<Store> stores = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (stores.isEmpty()) {
            logger.warn("No stores found after filtering");
        } else {
            logger.info("Found {} stores after filtering", stores.size());
        }

        return new PageImpl<>(stores, pageable, searchHits.getTotalHits());
    }

    public List<String> autocomplete(String input) {
        logger.info("Generating autocomplete suggestions for input: {}", input);

        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            b.should(s -> s.multiMatch(mm -> mm
                                    .query(input.toLowerCase())
                                    .fields("name", "description")
                                    .fuzziness("AUTO")
                                    .prefixLength(1)
                            ));
                            return b;
                        })
                )
                .build();

        SearchHits<Store> searchHits = elasticsearchTemplate.search(queryBuilder, Store.class);
        List<String> suggestions = new ArrayList<>();
        for (SearchHit<Store> hit : searchHits.getSearchHits()) {
            Store store = hit.getContent();
            suggestions.add(store.getName());
            suggestions.add(store.getDescription());
        }

        if (suggestions.isEmpty()) {
            logger.warn("No autocomplete suggestions found for input: {}", input);
        } else {
            logger.info("Found {} autocomplete suggestions", suggestions.size());
        }

        return suggestions.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }
}

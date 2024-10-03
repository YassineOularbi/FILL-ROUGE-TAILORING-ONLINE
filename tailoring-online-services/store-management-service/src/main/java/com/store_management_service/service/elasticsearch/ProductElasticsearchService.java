package com.store_management_service.service.elasticsearch;

import com.store_management_service.model.Product;
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
public class ProductElasticsearchService {

    private static final Logger logger = LoggerFactory.getLogger(ProductElasticsearchService.class);
    private final ElasticsearchTemplate elasticsearchTemplate;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<Product> search(String input, int page, int size, String sortField, String sortDirection) {
        logger.info("Searching for products with input: {}, page: {}, size: {}, sortField: {}, sortDirection: {}", input, page, size, sortField, sortDirection);

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
                                                .fields("name^3", "description", "category", "historicalStory")
                                                .fuzziness("AUTO")
                                                .prefixLength(2)
                                        )
                                )
                        )
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<Product> searchHits = elasticsearchTemplate.search(query, Product.class);
        List<Product> products = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (products.isEmpty()) {
            logger.warn("No products found for input: {}", input);
        } else {
            logger.info("Found {} products", products.size());
        }

        return new PageImpl<>(products, pageable, searchHits.getTotalHits());
    }

    public Page<Product> filter(int page, int size, String sortField, String sortDirection, String categoryFilter, String nameFilter) {
        logger.info("Filtering products with filters: categoryFilter={}, nameFilter={}", categoryFilter, nameFilter);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size);

        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            if (categoryFilter != null && !categoryFilter.isEmpty()) {
                                b.filter(f -> f.term(t -> t.field("category").value(categoryFilter)));
                            }
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

        SearchHits<Product> searchHits = elasticsearchTemplate.search(queryBuilder, Product.class);
        List<Product> products = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (products.isEmpty()) {
            logger.warn("No products found after filtering");
        } else {
            logger.info("Found {} products after filtering", products.size());
        }

        return new PageImpl<>(products, pageable, searchHits.getTotalHits());
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

        SearchHits<Product> searchHits = elasticsearchTemplate.search(queryBuilder, Product.class);
        List<String> suggestions = new ArrayList<>();
        for (SearchHit<Product> hit : searchHits.getSearchHits()) {
            Product product = hit.getContent();
            suggestions.add(product.getName());
            suggestions.add(product.getDescription());
        }

        if (suggestions.isEmpty()) {
            logger.warn("No autocomplete suggestions found for input: {}", input);
        } else {
            logger.info("Found {} autocomplete suggestions", suggestions.size());
        }

        return suggestions.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }
}

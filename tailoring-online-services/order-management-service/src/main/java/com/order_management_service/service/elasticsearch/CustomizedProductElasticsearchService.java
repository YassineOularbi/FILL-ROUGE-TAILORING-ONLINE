package com.order_management_service.service.elasticsearch;

import com.order_management_service.model.CustomizedProduct;
import com.order_management_service.repository.elasticsearch.CustomizedProductElasticsearchRepository;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomizedProductElasticsearchService {

    private static final Logger logger = LoggerFactory.getLogger(CustomizedProductElasticsearchService.class);
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CustomizedProductElasticsearchRepository customizedProductElasticsearchRepository;
    private static final int MAX_PAGE_SIZE = 100;

    @PostPersist
    @PostUpdate
    public void handleAfterCreateOrUpdate(CustomizedProduct customizedProduct) {
        logger.info("Saving customized product: {}", customizedProduct);
        customizedProductElasticsearchRepository.save(customizedProduct);
    }

    @PostRemove
    public void handleAfterDelete(CustomizedProduct customizedProduct) {
        logger.info("Deleting customized product: {}", customizedProduct);
        customizedProductElasticsearchRepository.delete(customizedProduct);
    }

    public Page<CustomizedProduct> search(String input, int page, int size, String sortField, String sortDirection) {
        logger.info("Searching for customized products with input: {}, page: {}, size: {}, sortField: {}, sortDirection: {}", input, page, size, sortField, sortDirection);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .should(s -> s
                                        .multiMatch(mm -> mm
                                                .query(input)
                                                .fields("productId^3", "measurements", "options")
                                                .fuzziness("AUTO").prefixLength(2)
                                        )
                                )
                        )
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<CustomizedProduct> searchHits = elasticsearchTemplate.search(query, CustomizedProduct.class);
        List<CustomizedProduct> products = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (products.isEmpty()) {
            logger.warn("No customized products found for input: {}", input);
        } else {
            logger.info("Found {} customized products", products.size());
        }

        return new PageImpl<>(products, pageable, searchHits.getTotalHits());
    }

    public Page<CustomizedProduct> filter(int page, int size, String sortField, String sortDirection, String productIdFilter, String measurementFilter, String optionFilter) {
        logger.info("Filtering customized products with filters: productIdFilter={}, measurementFilter={}, optionFilter={}", productIdFilter, measurementFilter, optionFilter);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            if (productIdFilter != null && !productIdFilter.isEmpty()) {
                                b.should(s -> s
                                        .multiMatch(mm -> mm
                                                .query(productIdFilter)
                                                .fields("productId")
                                                .fuzziness("AUTO").prefixLength(2)
                                        ));
                            }
                            if (measurementFilter != null && !measurementFilter.isEmpty()) {
                                b.should(s -> s
                                        .multiMatch(mm -> mm
                                                .query(measurementFilter)
                                                .fields("measurements")
                                                .fuzziness("AUTO").prefixLength(2)
                                        ));
                            }
                            if (optionFilter != null && !optionFilter.isEmpty()) {
                                b.should(s -> s
                                        .multiMatch(mm -> mm
                                                .query(optionFilter)
                                                .fields("options")
                                                .fuzziness("AUTO").prefixLength(2)
                                        ));
                            }
                            return b;
                        })
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<CustomizedProduct> searchHits = elasticsearchTemplate.search(queryBuilder, CustomizedProduct.class);
        List<CustomizedProduct> products = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (products.isEmpty()) {
            logger.warn("No customized products found after filtering");
        } else {
            logger.info("Found {} customized products after filtering", products.size());
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
                                    .fields("productId", "measurements", "options")
                                    .fuzziness("AUTO")
                                    .prefixLength(1)
                            ));
                            return b;
                        })
                )
                .build();

        SearchHits<CustomizedProduct> searchHits = elasticsearchTemplate.search(queryBuilder, CustomizedProduct.class);
        List<String> suggestions = new ArrayList<>();
        for (SearchHit<CustomizedProduct> hit : searchHits.getSearchHits()) {
            CustomizedProduct product = hit.getContent();
            suggestions.add(product.getProductId().toString());
            if (product.getMeasurements() != null) {
                product.getMeasurements().forEach(m -> suggestions.add(m.toString()));
            }
            if (product.getOptions() != null) {
                product.getOptions().forEach(o -> suggestions.add(o.toString()));
            }
        }

        if (suggestions.isEmpty()) {
            logger.warn("No autocomplete suggestions found for input: {}", input);
        } else {
            logger.info("Found {} autocomplete suggestions", suggestions.size());
        }

        return suggestions.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }
}

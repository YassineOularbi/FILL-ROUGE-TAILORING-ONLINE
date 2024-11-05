package com.order_management_service.service.elasticsearch;

import com.order_management_service.model.CustomizedOption;
import com.order_management_service.repository.elasticsearch.CustomizedOptionElasticsearchRepository;
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
public class CustomizedOptionElasticsearchService {

    private static final Logger logger = LoggerFactory.getLogger(CustomizedOptionElasticsearchService.class);
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CustomizedOptionElasticsearchRepository customizedOptionElasticsearchRepository;
    private static final int MAX_PAGE_SIZE = 100;

    @PostPersist
    @PostUpdate
    public void handleAfterCreateOrUpdate(CustomizedOption customizedOption) {
        logger.info("Saving customized option: {}", customizedOption);
        customizedOptionElasticsearchRepository.save(customizedOption);
    }

    @PostRemove
    public void handleAfterDelete(CustomizedOption customizedOption) {
        logger.info("Deleting customized option: {}", customizedOption);
        customizedOptionElasticsearchRepository.delete(customizedOption);
    }

    public Page<CustomizedOption> search(String input, int page, int size, String sortField, String sortDirection) {
        logger.info("Searching for customized options with input: {}, page: {}, size: {}, sortField: {}, sortDirection: {}", input, page, size, sortField, sortDirection);

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
                                                .fields("type^3", "materialId^2", "product")
                                                .fuzziness("AUTO").prefixLength(2)
                                        )
                                )
                        )
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<CustomizedOption> searchHits = elasticsearchTemplate.search(query, CustomizedOption.class);
        List<CustomizedOption> options = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (options.isEmpty()) {
            logger.warn("No customized options found for input: {}", input);
        } else {
            logger.info("Found {} customized options", options.size());
        }

        return new PageImpl<>(options, pageable, searchHits.getTotalHits());
    }

    public Page<CustomizedOption> filter(int page, int size, String sortField, String sortDirection, String typeFilter, Long materialIdFilter, Long productIdFilter) {
        logger.info("Filtering customized options with filters: typeFilter={}, materialIdFilter={}, productIdFilter={}", typeFilter, materialIdFilter, productIdFilter);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            if (typeFilter != null && !typeFilter.isEmpty()) {
                                b.should(s -> s
                                        .multiMatch(mm -> mm
                                                .query(typeFilter)
                                                .fields("type")
                                                .fuzziness("AUTO").prefixLength(2)
                                        ));
                            }
                            if (materialIdFilter != null) {
                                b.filter(f -> f.term(t -> t.field("materialId").value(materialIdFilter)));
                            }
                            if (productIdFilter != null) {
                                b.filter(f -> f.term(t -> t.field("product.id").value(productIdFilter)));
                            }
                            return b;
                        })
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<CustomizedOption> searchHits = elasticsearchTemplate.search(queryBuilder, CustomizedOption.class);
        List<CustomizedOption> options = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (options.isEmpty()) {
            logger.warn("No customized options found after filtering");
        } else {
            logger.info("Found {} customized options after filtering", options.size());
        }

        return new PageImpl<>(options, pageable, searchHits.getTotalHits());
    }

    public List<String> autocomplete(String input) {
        logger.info("Generating autocomplete suggestions for input: {}", input);
        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            b.should(s -> s.multiMatch(mm -> mm
                                    .query(input.toLowerCase())
                                    .fields("type", "materialId")
                                    .fuzziness("AUTO")
                                    .prefixLength(1)
                            ));
                            return b;
                        })
                )
                .build();

        SearchHits<CustomizedOption> searchHits = elasticsearchTemplate.search(queryBuilder, CustomizedOption.class);
        List<String> suggestions = new ArrayList<>();
        for (SearchHit<CustomizedOption> hit : searchHits.getSearchHits()) {
            CustomizedOption option = hit.getContent();
            suggestions.add(option.getType().toString());
            suggestions.add(option.getMaterialId().toString());
        }

        if (suggestions.isEmpty()) {
            logger.warn("No autocomplete suggestions found for input: {}", input);
        } else {
            logger.info("Found {} autocomplete suggestions", suggestions.size());
        }

        return suggestions.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }
}

package com.store_management_service.service.elasticsearch;

import co.elastic.clients.json.JsonData;
import com.store_management_service.model.MaterialOption;
import com.store_management_service.model.MaterialOptionKey;
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
public class MaterialOptionElasticsearchService {

    private static final Logger logger = LoggerFactory.getLogger(MaterialOptionElasticsearchService.class);
    private final ElasticsearchTemplate elasticsearchTemplate;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<MaterialOption> search(String input, int page, int size, String sortField, String sortDirection) {
        logger.info("Searching for material options with input: {}, page: {}, size: {}, sortField: {}, sortDirection: {}", input, page, size, sortField, sortDirection);

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
                                                .fields("material.id", "option.id")
                                                .fuzziness("AUTO")
                                                .prefixLength(2)
                                        )
                                )
                        )
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<MaterialOption> searchHits = elasticsearchTemplate.search(query, MaterialOption.class);
        List<MaterialOption> materialOptions = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (materialOptions.isEmpty()) {
            logger.warn("No material options found for input: {}", input);
        } else {
            logger.info("Found {} material options", materialOptions.size());
        }

        return new PageImpl<>(materialOptions, pageable, searchHits.getTotalHits());
    }

    public Page<MaterialOption> filter(int page, int size, String sortField, String sortDirection, String materialIdFilter, String optionIdFilter) {
        logger.info("Filtering material options with filters: materialIdFilter={}, optionIdFilter={}", materialIdFilter, optionIdFilter);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size);

        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            if (materialIdFilter != null && !materialIdFilter.isEmpty()) {
                                b.filter(f -> f.term(t -> t.field("material.id").value(materialIdFilter)));
                            }
                            if (optionIdFilter != null && !optionIdFilter.isEmpty()) {
                                b.filter(f -> f.term(t -> t.field("option.id").value(optionIdFilter)));
                            }
                            return b;
                        })
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<MaterialOption> searchHits = elasticsearchTemplate.search(queryBuilder, MaterialOption.class);
        List<MaterialOption> materialOptions = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (materialOptions.isEmpty()) {
            logger.warn("No material options found after filtering");
        } else {
            logger.info("Found {} material options after filtering", materialOptions.size());
        }

        return new PageImpl<>(materialOptions, pageable, searchHits.getTotalHits());
    }

    public List<String> autocomplete(String input) {
        logger.info("Generating autocomplete suggestions for input: {}", input);

        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            b.should(s -> s.multiMatch(mm -> mm
                                    .query(input.toLowerCase())
                                    .fields("material.id", "option.id")
                                    .fuzziness("AUTO")
                                    .prefixLength(1)
                            ));
                            return b;
                        })
                )
                .build();

        SearchHits<MaterialOption> searchHits = elasticsearchTemplate.search(queryBuilder, MaterialOption.class);
        List<String> suggestions = new ArrayList<>();
        for (SearchHit<MaterialOption> hit : searchHits.getSearchHits()) {
            MaterialOption materialOption = hit.getContent();
            suggestions.add(materialOption.getId().toString());
        }

        if (suggestions.isEmpty()) {
            logger.warn("No autocomplete suggestions found for input: {}", input);
        } else {
            logger.info("Found {} autocomplete suggestions", suggestions.size());
        }

        return suggestions.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }
}

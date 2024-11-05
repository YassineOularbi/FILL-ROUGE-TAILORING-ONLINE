package com.store_management_service.service.elasticsearch;

import com.store_management_service.model.CustomizableOption;
import com.store_management_service.repository.elasticsearch.CustomizableOptionElasticsearchRepository;
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
public class CustomizableOptionElasticsearchService {

    private static final Logger logger = LoggerFactory.getLogger(CustomizableOptionElasticsearchService.class);
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CustomizableOptionElasticsearchRepository customizableOptionElasticsearchRepository;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<CustomizableOption> search(String input, int page, int size, String sortField, String sortDirection) {
        logger.info("Searching for customizable options with input: {}, page: {}, size: {}, sortField: {}, sortDirection: {}", input, page, size, sortField, sortDirection);

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
                                                .fields("type", "model.id") // Adjust fields as necessary
                                                .fuzziness("AUTO")
                                                .prefixLength(2)
                                        )
                                )
                        )
                )
                .withPageable(pageable)
                .build();

        SearchHits<CustomizableOption> searchHits = elasticsearchTemplate.search(query, CustomizableOption.class);
        List<CustomizableOption> options = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (options.isEmpty()) {
            logger.warn("No customizable options found for input: {}", input);
        } else {
            logger.info("Found {} customizable options", options.size());
        }

        return new PageImpl<>(options, pageable, searchHits.getTotalHits());
    }

    public void index(CustomizableOption option) {
        logger.info("Indexing customizable option: {}", option);
        customizableOptionElasticsearchRepository.save(option);
    }

    public void delete(CustomizableOption option) {
        logger.info("Deleting customizable option: {}", option);
        customizableOptionElasticsearchRepository.delete(option);
    }

    public Page<CustomizableOption> filter(int page, int size, String sortField, String sortDirection, String typeFilter, String modelIdFilter) {
        logger.info("Filtering customizable options with filters: typeFilter={}, modelIdFilter={}", typeFilter, modelIdFilter);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            if (typeFilter != null && !typeFilter.isEmpty()) {
                                b.filter(f -> f.term(t -> t.field("type").value(typeFilter)));
                            }
                            if (modelIdFilter != null && !modelIdFilter.isEmpty()) {
                                b.filter(f -> f.term(t -> t.field("model.id").value(modelIdFilter)));
                            }
                            return b;
                        })
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<CustomizableOption> searchHits = elasticsearchTemplate.search(queryBuilder, CustomizableOption.class);
        List<CustomizableOption> options = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (options.isEmpty()) {
            logger.warn("No customizable options found after filtering");
        } else {
            logger.info("Found {} customizable options after filtering", options.size());
        }

        return new PageImpl<>(options, pageable, searchHits.getTotalHits());
    }


    public List<String> autocomplete(String input) {
        logger.info("Generating autocomplete suggestions for input: {}", input);

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .should(s -> s.multiMatch(mm -> mm
                                        .query(input.toLowerCase())
                                        .fields("type", "model.id")
                                        .fuzziness("AUTO")
                                        .prefixLength(1)
                                ))
                        )
                )
                .build();

        SearchHits<CustomizableOption> searchHits = elasticsearchTemplate.search(query, CustomizableOption.class);
        List<String> suggestions = new ArrayList<>();
        for (SearchHit<CustomizableOption> hit : searchHits.getSearchHits()) {
            CustomizableOption option = hit.getContent();
            suggestions.add(option.getType().toString());
        }

        if (suggestions.isEmpty()) {
            logger.warn("No autocomplete suggestions found for input: {}", input);
        } else {
            logger.info("Found {} autocomplete suggestions", suggestions.size());
        }

        return suggestions.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }
}

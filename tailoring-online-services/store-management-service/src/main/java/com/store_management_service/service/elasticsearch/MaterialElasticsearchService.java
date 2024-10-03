package com.store_management_service.service.elasticsearch;

import com.store_management_service.model.Material;
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
public class MaterialElasticsearchService {

    private static final Logger logger = LoggerFactory.getLogger(MaterialElasticsearchService.class);
    private final ElasticsearchTemplate elasticsearchTemplate;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<Material> search(String input, int page, int size, String sortField, String sortDirection) {
        logger.info("Searching for materials with input: {}, page: {}, size: {}, sortField: {}, sortDirection: {}", input, page, size, sortField, sortDirection);

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
                                                .fields("name^3", "description", "type")
                                                .fuzziness("AUTO")
                                                .prefixLength(2)
                                        )
                                )
                        )
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<Material> searchHits = elasticsearchTemplate.search(query, Material.class);
        List<Material> materials = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (materials.isEmpty()) {
            logger.warn("No materials found for input: {}", input);
        } else {
            logger.info("Found {} materials", materials.size());
        }

        return new PageImpl<>(materials, pageable, searchHits.getTotalHits());
    }

    public Page<Material> filter(int page, int size, String sortField, String sortDirection, String nameFilter, String typeFilter) {
        logger.info("Filtering materials with filters: nameFilter={}, typeFilter={}", nameFilter, typeFilter);

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
                            if (typeFilter != null && !typeFilter.isEmpty()) {
                                b.filter(f -> f.term(t -> t.field("type").value(typeFilter)));
                            }
                            return b;
                        })
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<Material> searchHits = elasticsearchTemplate.search(queryBuilder, Material.class);
        List<Material> materials = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (materials.isEmpty()) {
            logger.warn("No materials found after filtering");
        } else {
            logger.info("Found {} materials after filtering", materials.size());
        }

        return new PageImpl<>(materials, pageable, searchHits.getTotalHits());
    }

    public List<String> autocomplete(String input) {
        logger.info("Generating autocomplete suggestions for input: {}", input);

        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            b.should(s -> s.multiMatch(mm -> mm
                                    .query(input.toLowerCase())
                                    .fields("name", "description", "type")
                                    .fuzziness("AUTO")
                                    .prefixLength(1)
                            ));
                            return b;
                        })
                )
                .build();

        SearchHits<Material> searchHits = elasticsearchTemplate.search(queryBuilder, Material.class);
        List<String> suggestions = new ArrayList<>();
        for (SearchHit<Material> hit : searchHits.getSearchHits()) {
            Material material = hit.getContent();
            suggestions.add(material.getName());
            suggestions.add(material.getDescription());
            suggestions.add(material.getType().toString());
        }

        if (suggestions.isEmpty()) {
            logger.warn("No autocomplete suggestions found for input: {}", input);
        } else {
            logger.info("Found {} autocomplete suggestions", suggestions.size());
        }

        return suggestions.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }
}

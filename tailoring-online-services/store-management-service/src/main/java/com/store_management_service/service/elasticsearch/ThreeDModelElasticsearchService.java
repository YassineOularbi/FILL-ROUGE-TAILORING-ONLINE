package com.store_management_service.service.elasticsearch;

import com.store_management_service.model.ThreeDModel;
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
public class ThreeDModelElasticsearchService {

    private static final Logger logger = LoggerFactory.getLogger(ThreeDModelElasticsearchService.class);
    private final ElasticsearchTemplate elasticsearchTemplate;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<ThreeDModel> search(String input, int page, int size, String sortField, String sortDirection) {
        logger.info("Searching for 3D models with input: {}, page: {}, size: {}, sortField: {}, sortDirection: {}", input, page, size, sortField, sortDirection);

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
                                                .fields("product.name^3", "product.description")
                                                .fuzziness("AUTO")
                                                .prefixLength(2)
                                        )
                                )
                        )
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<ThreeDModel> searchHits = elasticsearchTemplate.search(query, ThreeDModel.class);
        List<ThreeDModel> models = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (models.isEmpty()) {
            logger.warn("No 3D models found for input: {}", input);
        } else {
            logger.info("Found {} 3D models", models.size());
        }

        return new PageImpl<>(models, pageable, searchHits.getTotalHits());
    }

    public Page<ThreeDModel> filter(int page, int size, String sortField, String sortDirection, String nameFilter) {
        logger.info("Filtering 3D models with filters: nameFilter={}", nameFilter);

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
                                        .fields("product.name")
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

        SearchHits<ThreeDModel> searchHits = elasticsearchTemplate.search(queryBuilder, ThreeDModel.class);
        List<ThreeDModel> models = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (models.isEmpty()) {
            logger.warn("No 3D models found after filtering");
        } else {
            logger.info("Found {} 3D models after filtering", models.size());
        }

        return new PageImpl<>(models, pageable, searchHits.getTotalHits());
    }

    public List<String> autocomplete(String input) {
        logger.info("Generating autocomplete suggestions for input: {}", input);

        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            b.should(s -> s.multiMatch(mm -> mm
                                    .query(input.toLowerCase())
                                    .fields("product.name", "product.description")
                                    .fuzziness("AUTO")
                                    .prefixLength(1)
                            ));
                            return b;
                        })
                )
                .build();

        SearchHits<ThreeDModel> searchHits = elasticsearchTemplate.search(queryBuilder, ThreeDModel.class);
        List<String> suggestions = new ArrayList<>();
        for (SearchHit<ThreeDModel> hit : searchHits.getSearchHits()) {
            ThreeDModel model = hit.getContent();
            suggestions.add(model.getProduct().getName());
            suggestions.add(model.getProduct().getDescription());
        }

        if (suggestions.isEmpty()) {
            logger.warn("No autocomplete suggestions found for input: {}", input);
        } else {
            logger.info("Found {} autocomplete suggestions", suggestions.size());
        }

        return suggestions.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }
}

package com.store_management_service.service.elasticsearch;

import co.elastic.clients.json.JsonData;
import com.store_management_service.model.Measurement;
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
public class MeasurementElasticsearchService {

    private static final Logger logger = LoggerFactory.getLogger(MeasurementElasticsearchService.class);
    private final ElasticsearchTemplate elasticsearchTemplate;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<Measurement> search(String input, int page, int size, String sortField, String sortDirection) {
        logger.info("Searching for measurements with input: {}, page: {}, size: {}, sortField: {}, sortDirection: {}", input, page, size, sortField, sortDirection);

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
                                                .fields("name^3", "description")
                                                .fuzziness("AUTO")
                                                .prefixLength(2)
                                        )
                                )
                        )
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<Measurement> searchHits = elasticsearchTemplate.search(query, Measurement.class);
        List<Measurement> measurements = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (measurements.isEmpty()) {
            logger.warn("No measurements found for input: {}", input);
        } else {
            logger.info("Found {} measurements", measurements.size());
        }

        return new PageImpl<>(measurements, pageable, searchHits.getTotalHits());
    }

    public Page<Measurement> filter(int page, int size, String sortField, String sortDirection, String nameFilter, String descriptionFilter) {
        logger.info("Filtering measurements with filters: nameFilter={}, descriptionFilter={}", nameFilter, descriptionFilter);

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
                            if (descriptionFilter != null && !descriptionFilter.isEmpty()) {
                                b.filter(f -> f.term(t -> t.field("description").value(descriptionFilter)));
                            }
                            return b;
                        })
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<Measurement> searchHits = elasticsearchTemplate.search(queryBuilder, Measurement.class);
        List<Measurement> measurements = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (measurements.isEmpty()) {
            logger.warn("No measurements found after filtering");
        } else {
            logger.info("Found {} measurements after filtering", measurements.size());
        }

        return new PageImpl<>(measurements, pageable, searchHits.getTotalHits());
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

        SearchHits<Measurement> searchHits = elasticsearchTemplate.search(queryBuilder, Measurement.class);
        List<String> suggestions = new ArrayList<>();
        for (SearchHit<Measurement> hit : searchHits.getSearchHits()) {
            Measurement measurement = hit.getContent();
            suggestions.add(measurement.getName());
            suggestions.add(measurement.getDescription());
        }

        if (suggestions.isEmpty()) {
            logger.warn("No autocomplete suggestions found for input: {}", input);
        } else {
            logger.info("Found {} autocomplete suggestions", suggestions.size());
        }

        return suggestions.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }
}

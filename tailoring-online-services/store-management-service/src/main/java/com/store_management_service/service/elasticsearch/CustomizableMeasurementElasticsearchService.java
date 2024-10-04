package com.store_management_service.service.elasticsearch;

import com.store_management_service.model.CustomizableMeasurement;
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
public class CustomizableMeasurementElasticsearchService {

    private static final Logger logger = LoggerFactory.getLogger(CustomizableMeasurementElasticsearchService.class);
    private final ElasticsearchTemplate elasticsearchTemplate;
    private static final int MAX_PAGE_SIZE = 100;

    @PostPersist
    @PostUpdate
    public void handleAfterCreateOrUpdate(CustomizableMeasurement customizableMeasurement) {
        logger.info("Saving customizable measurement: {}", customizableMeasurement);
        elasticsearchTemplate.save(customizableMeasurement);
    }

    @PostRemove
    public void handleAfterDelete(CustomizableMeasurement customizableMeasurement) {
        logger.info("Deleting customizable measurement: {}", customizableMeasurement);
        elasticsearchTemplate.delete(customizableMeasurement);
    }

    public Page<CustomizableMeasurement> search(String input, int page, int size, String sortField, String sortDirection) {
        logger.info("Searching for customizable measurements with input: {}, page: {}, size: {}, sortField: {}, sortDirection: {}", input, page, size, sortField, sortDirection);

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
                                                .fields("id.modelId", "id.measurementId", "measurement.name")
                                                .fuzziness("AUTO")
                                                .prefixLength(2)
                                        )
                                )
                        )
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<CustomizableMeasurement> searchHits = elasticsearchTemplate.search(query, CustomizableMeasurement.class);
        List<CustomizableMeasurement> customizableMeasurements = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (customizableMeasurements.isEmpty()) {
            logger.warn("No customizable measurements found for input: {}", input);
        } else {
            logger.info("Found {} customizable measurements", customizableMeasurements.size());
        }

        return new PageImpl<>(customizableMeasurements, pageable, searchHits.getTotalHits());
    }

    public Page<CustomizableMeasurement> filter(int page, int size, String sortField, String sortDirection, String modelIdFilter, String measurementNameFilter) {
        logger.info("Filtering customizable measurements with filters: modelIdFilter={}, measurementNameFilter={}", modelIdFilter, measurementNameFilter);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            if (modelIdFilter != null && !modelIdFilter.isEmpty()) {
                                b.should(s -> s.multiMatch(mm -> mm
                                        .query(modelIdFilter)
                                        .fields("id.modelId")
                                        .fuzziness("AUTO").prefixLength(2)
                                ));
                            }
                            if (measurementNameFilter != null && !measurementNameFilter.isEmpty()) {
                                b.should(s -> s.multiMatch(mm -> mm
                                        .query(measurementNameFilter)
                                        .fields("measurement.name")
                                        .fuzziness("AUTO").prefixLength(2)
                                ));
                            }
                            return b;
                        })
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<CustomizableMeasurement> searchHits = elasticsearchTemplate.search(queryBuilder, CustomizableMeasurement.class);
        List<CustomizableMeasurement> customizableMeasurements = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (customizableMeasurements.isEmpty()) {
            logger.warn("No customizable measurements found after filtering");
        } else {
            logger.info("Found {} customizable measurements after filtering", customizableMeasurements.size());
        }

        return new PageImpl<>(customizableMeasurements, pageable, searchHits.getTotalHits());
    }

    public List<String> autocomplete(String input) {
        logger.info("Generating autocomplete suggestions for input: {}", input);

        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            b.should(s -> s.multiMatch(mm -> mm
                                    .query(input.toLowerCase())
                                    .fields("id.modelId", "id.measurementId", "measurement.name")
                                    .fuzziness("AUTO")
                                    .prefixLength(1)
                            ));
                            return b;
                        })
                )
                .build();

        SearchHits<CustomizableMeasurement> searchHits = elasticsearchTemplate.search(queryBuilder, CustomizableMeasurement.class);
        List<String> suggestions = new ArrayList<>();
        for (SearchHit<CustomizableMeasurement> hit : searchHits.getSearchHits()) {
            CustomizableMeasurement customizableMeasurement = hit.getContent();
            suggestions.add(customizableMeasurement.getId().getModelId().toString());
            suggestions.add(customizableMeasurement.getMeasurement().getName());
        }

        if (suggestions.isEmpty()) {
            logger.warn("No autocomplete suggestions found for input: {}", input);
        } else {
            logger.info("Found {} autocomplete suggestions", suggestions.size());
        }

        return suggestions.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }
}

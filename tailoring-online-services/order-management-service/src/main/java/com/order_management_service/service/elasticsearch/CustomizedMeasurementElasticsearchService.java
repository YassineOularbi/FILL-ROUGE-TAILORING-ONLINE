package com.order_management_service.service.elasticsearch;

import com.order_management_service.model.CustomizedMeasurement;
import com.order_management_service.repository.elasticsearch.CustomizedMeasurementElasticsearchRepository;
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
public class CustomizedMeasurementElasticsearchService {

    private static final Logger logger = LoggerFactory.getLogger(CustomizedMeasurementElasticsearchService.class);
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CustomizedMeasurementElasticsearchRepository customizedMeasurementElasticsearchRepository;
    private static final int MAX_PAGE_SIZE = 100;

    @PostPersist
    @PostUpdate
    public void handleAfterCreateOrUpdate(CustomizedMeasurement customizedMeasurement) {
        logger.info("Saving customized measurement: {}", customizedMeasurement);
        customizedMeasurementElasticsearchRepository.save(customizedMeasurement);
    }

    @PostRemove
    public void handleAfterDelete(CustomizedMeasurement customizedMeasurement) {
        logger.info("Deleting customized measurement: {}", customizedMeasurement);
        customizedMeasurementElasticsearchRepository.delete(customizedMeasurement);
    }

    public Page<CustomizedMeasurement> search(String input, int page, int size, String sortField, String sortDirection) {
        logger.info("Searching for customized measurements with input: {}, page: {}, size: {}, sortField: {}, sortDirection: {}", input, page, size, sortField, sortDirection);

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
                                                .fields("measurementId^3", "value^2", "unit")
                                                .fuzziness("AUTO").prefixLength(2)
                                        )
                                )
                        )
                )
                .withPageable(pageable)
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<CustomizedMeasurement> searchHits = elasticsearchTemplate.search(query, CustomizedMeasurement.class);
        List<CustomizedMeasurement> measurements = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (measurements.isEmpty()) {
            logger.warn("No customized measurements found for input: {}", input);
        } else {
            logger.info("Found {} customized measurements", measurements.size());
        }

        return new PageImpl<>(measurements, pageable, searchHits.getTotalHits());
    }

    public Page<CustomizedMeasurement> filter(int page, int size, String sortField, String sortDirection, Long measurementIdFilter, Double valueFilter, String unitFilter, Long productIdFilter) {
        logger.info("Filtering customized measurements with filters: measurementIdFilter={}, valueFilter={}, unitFilter={}, productIdFilter={}", measurementIdFilter, valueFilter, unitFilter, productIdFilter);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            if (measurementIdFilter != null) {
                                b.filter(f -> f.term(t -> t.field("measurementId").value(measurementIdFilter)));
                            }
                            if (valueFilter != null) {
                                b.filter(f -> f.term(t -> t.field("value").value(valueFilter)));
                            }
                            if (unitFilter != null && !unitFilter.isEmpty()) {
                                b.filter(f -> f.term(t -> t.field("unit").value(unitFilter.toLowerCase())));
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

        SearchHits<CustomizedMeasurement> searchHits = elasticsearchTemplate.search(queryBuilder, CustomizedMeasurement.class);
        List<CustomizedMeasurement> measurements = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (measurements.isEmpty()) {
            logger.warn("No customized measurements found after filtering");
        } else {
            logger.info("Found {} customized measurements after filtering", measurements.size());
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
                                    .fields("measurementId", "unit")
                                    .fuzziness("AUTO")
                                    .prefixLength(1)
                            ));
                            return b;
                        })
                )
                .build();

        SearchHits<CustomizedMeasurement> searchHits = elasticsearchTemplate.search(queryBuilder, CustomizedMeasurement.class);
        List<String> suggestions = new ArrayList<>();
        for (SearchHit<CustomizedMeasurement> hit : searchHits.getSearchHits()) {
            CustomizedMeasurement measurement = hit.getContent();
            suggestions.add(measurement.getMeasurementId().toString());
            suggestions.add(measurement.getUnit().toString());
        }

        if (suggestions.isEmpty()) {
            logger.warn("No autocomplete suggestions found for input: {}", input);
        } else {
            logger.info("Found {} autocomplete suggestions", suggestions.size());
        }

        return suggestions.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }
}

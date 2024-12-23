package com.localization_shipping_service.service.elasticsearch;

import com.localization_shipping_service.model.Address;
import com.localization_shipping_service.repository.elasticsearch.AddressElasticsearchRepository;
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
public class AddressElasticsearchService {

    private static final Logger logger = LoggerFactory.getLogger(AddressElasticsearchService.class);
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AddressElasticsearchRepository addressElasticsearchRepository;
    private static final int MAX_PAGE_SIZE = 100;

    @PostPersist
    @PostUpdate
    public void handleAfterCreateOrUpdate(Address address) {
        logger.info("Saving address: {}", address);
        addressElasticsearchRepository.save(address);
    }

    @PostRemove
    public void handleAfterDelete(Address address) {
        logger.info("Deleting address: {}", address);
        addressElasticsearchRepository.delete(address);
    }

    public Page<Address> search(String input, int page, int size, String sortField, String sortDirection) {
        logger.info("Searching for addresses with input: {}, page: {}, size: {}, sortField: {}, sortDirection: {}", input, page, size, sortField, sortDirection);

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
                                                .fields("address^3", "suite", "city^2", "province", "country^1")
                                                .fuzziness("AUTO").prefixLength(2)
                                        )
                                )
                                .should(s -> s.wildcard(w -> w.field("address").wildcard("*" + input.toLowerCase() + "*")))
                                .should(s -> s.wildcard(w -> w.field("suite").wildcard("*" + input.toLowerCase() + "*")))
                                .should(s -> s.wildcard(w -> w.field("city").wildcard("*" + input.toLowerCase() + "*")))
                                .should(s -> s.wildcard(w -> w.field("province").wildcard("*" + input.toLowerCase() + "*")))
                                .should(s -> s.wildcard(w -> w.field("country").wildcard("*" + input.toLowerCase() + "*")))
                        )
                )
                .withPageable(PageRequest.of(page, size))
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<Address> searchHits = elasticsearchTemplate.search(query, Address.class);
        List<Address> addresses = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (addresses.isEmpty()) {
            logger.warn("No addresses found for input: {}", input);
        } else {
            logger.info("Found {} addresses", addresses.size());
        }

        return new PageImpl<>(addresses, pageable, searchHits.getTotalHits());
    }

    public Page<Address> filter(int page, int size, String sortField, String sortDirection, String addressFilter, String suiteFilter, String cityFilter, String provinceFilter, String countryFilter, Boolean defaultFilter, String zipCodeFilter) {
        logger.info("Filtering addresses with filters: addressFilter={}, suiteFilter={}, cityFilter={}, provinceFilter={}, countryFilter={}, defaultFilter={}, zipCodeFilter={}", addressFilter, suiteFilter, cityFilter, provinceFilter, countryFilter, defaultFilter, zipCodeFilter);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            if (addressFilter != null && !addressFilter.isEmpty()) {
                                b.should(s -> s
                                                .multiMatch(mm -> mm
                                                        .query(addressFilter.toLowerCase())
                                                        .fields("address")
                                                        .fuzziness("AUTO").prefixLength(2)
                                                ))
                                        .should(s -> s.wildcard(w -> w.field("address").wildcard("*" + addressFilter.toLowerCase() + "*")));
                            }
                            if (suiteFilter != null && !suiteFilter.isEmpty()) {
                                b.should(s -> s
                                                .multiMatch(mm -> mm
                                                        .query(suiteFilter.toLowerCase())
                                                        .fields("suite")
                                                        .fuzziness("AUTO").prefixLength(2)
                                                ))
                                        .should(s -> s.wildcard(w -> w.field("suite").wildcard("*" + suiteFilter.toLowerCase() + "*")));
                            }
                            if (zipCodeFilter != null && !zipCodeFilter.isEmpty()) {
                                b.should(s -> s
                                                .multiMatch(mm -> mm
                                                        .query(zipCodeFilter.toLowerCase())
                                                        .fields("zipCode")
                                                        .fuzziness("AUTO").prefixLength(1)
                                                ))
                                        .should(s -> s.wildcard(w -> w.field("zipCode").wildcard("*" + zipCodeFilter.toLowerCase() + "*")));
                            }
                            if (cityFilter != null && !cityFilter.isEmpty()) {
                                b.filter(f -> f.term(t -> t.field("city").value(cityFilter.toLowerCase())));
                            }
                            if (provinceFilter != null && !provinceFilter.isEmpty()) {
                                b.filter(f -> f.term(t -> t.field("province").value(provinceFilter.toLowerCase())));
                            }
                            if (countryFilter != null && !countryFilter.isEmpty()) {
                                b.filter(f -> f.term(t -> t.field("country").value(countryFilter.toLowerCase())));
                            }
                            if (defaultFilter != null) {
                                b.filter(f -> f.term(t -> t.field("isDefault").value(defaultFilter)));
                            }
                            return b;
                        })
                )
                .withPageable(PageRequest.of(page, size))
                .withSort(Sort.by(direction, sortField))
                .build();

        SearchHits<Address> searchHits = elasticsearchTemplate.search(queryBuilder, Address.class);
        List<Address> addresses = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        if (addresses.isEmpty()) {
            logger.warn("No addresses found after filtering");
        } else {
            logger.info("Found {} addresses after filtering", addresses.size());
        }

        return new PageImpl<>(addresses, pageable, searchHits.getTotalHits());
    }

    public List<String> autocomplete(String input) {
        logger.info("Generating autocomplete suggestions for input: {}", input);
        NativeQuery queryBuilder = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            b.should(s -> s.multiMatch(mm -> mm
                                    .query(input.toLowerCase())
                                    .fields("address", "suite", "city", "province", "country")
                                    .fuzziness("AUTO")
                                    .prefixLength(1)
                            ));

                            b.should(s -> s.wildcard(w -> w.field("address").wildcard("*" + input.toLowerCase() + "*")));
                            b.should(s -> s.wildcard(w -> w.field("suite").wildcard("*" + input.toLowerCase() + "*")));
                            b.should(s -> s.wildcard(w -> w.field("city").wildcard("*" + input.toLowerCase() + "*")));
                            b.should(s -> s.wildcard(w -> w.field("province").wildcard("*" + input.toLowerCase() + "*")));
                            b.should(s -> s.wildcard(w -> w.field("country").wildcard("*" + input.toLowerCase() + "*")));
                            return b;
                        })
                )
                .build();

        SearchHits<Address> searchHits = elasticsearchTemplate.search(queryBuilder, Address.class);
        List<String> suggestions = new ArrayList<>();
        for (SearchHit<Address> hit : searchHits.getSearchHits()) {
            Address address = hit.getContent();
            suggestions.add(address.getAddress());
            suggestions.add(address.getSuite());
            suggestions.add(address.getCity());
            suggestions.add(address.getProvince());
            suggestions.add(address.getCountry());
        }

        if (suggestions.isEmpty()) {
            logger.warn("No autocomplete suggestions found for input: {}", input);
        } else {
            logger.info("Found {} autocomplete suggestions", suggestions.size());
        }

        return suggestions.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }
}

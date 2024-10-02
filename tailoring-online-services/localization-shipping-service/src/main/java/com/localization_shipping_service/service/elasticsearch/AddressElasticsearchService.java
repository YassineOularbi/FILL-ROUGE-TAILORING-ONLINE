package com.localization_shipping_service.service.elasticsearch;

import com.localization_shipping_service.model.Address;
import com.localization_shipping_service.repository.elasticsearch.AddressElasticsearchRepository;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressElasticsearchService {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AddressElasticsearchRepository addressElasticsearchRepository;
    private static final int MAX_PAGE_SIZE = 100;

    @PostPersist
    @PostUpdate
    public void handleAfterCreateOrUpdate(Address address) {
        addressElasticsearchRepository.save(address);
    }

    @PostRemove
    public void handleAfterDelete(Address address) {
        addressElasticsearchRepository.delete(address);
    }

    public Page<Address> search(String input, int page, int size, String sortField, String sortDirection) {
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
        return new PageImpl<>(addresses, pageable, searchHits.getTotalHits());
    }

    public Page<Address> filter(int page, int size, String sortField, String sortDirection, String addressFilter, String suiteFilter, String cityFilter, String provinceFilter, String countryFilter, Boolean defaultFilter, String zipCodeFilter) {
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
        return new PageImpl<>(addresses, pageable, searchHits.getTotalHits());
    }


}
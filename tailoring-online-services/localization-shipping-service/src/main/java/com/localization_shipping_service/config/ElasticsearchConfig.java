package com.localization_shipping_service.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);

    @Bean
    public RestClient getRestClient() {
        logger.info("Creating RestClient for Elasticsearch at localhost:9200");
        return RestClient.builder(new HttpHost("localhost", 9200)).build();
    }

    @Bean
    public ElasticsearchTransport getElasticsearchTransport() {
        logger.info("Creating ElasticsearchTransport with JacksonJsonpMapper");
        return new RestClientTransport(getRestClient(), new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient getElasticsearchClient() {
        logger.info("Creating ElasticsearchClient");
        return new ElasticsearchClient(getElasticsearchTransport());
    }
}

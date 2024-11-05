package com.payment_banking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaRepositories(basePackages = "com.payment_banking_service.repository.jpa")
@EnableElasticsearchRepositories(basePackages = "com.payment_banking_service.repository.elasticsearch")
public class PaymentBankingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentBankingServiceApplication.class, args);
	}

}

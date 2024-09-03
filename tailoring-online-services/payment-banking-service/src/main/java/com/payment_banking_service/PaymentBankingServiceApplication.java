package com.payment_banking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PaymentBankingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentBankingServiceApplication.class, args);
	}

}

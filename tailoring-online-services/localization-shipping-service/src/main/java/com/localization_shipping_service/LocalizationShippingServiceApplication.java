package com.localization_shipping_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LocalizationShippingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalizationShippingServiceApplication.class, args);
	}

}

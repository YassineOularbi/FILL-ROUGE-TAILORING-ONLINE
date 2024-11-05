package com.loyalty_program_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LoyaltyProgramServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoyaltyProgramServiceApplication.class, args);
	}

}

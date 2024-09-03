package com.social_management_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SocialManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialManagementServiceApplication.class, args);
	}

}

package com.notification_mailing_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NotificationMailingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationMailingServiceApplication.class, args);
	}

}

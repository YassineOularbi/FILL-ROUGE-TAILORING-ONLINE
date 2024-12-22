package com.user_management_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private int port;

    // Capture the dynamic port at runtime
    @EventListener
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.port = event.getWebServer().getPort();
        System.out.println("Application started on dynamic port: " + port);
    }

    // Endpoint to expose the dynamic port
    @GetMapping("/dynamic-port")
    public String getDynamicPort() {
        return "Instance running on dynamic port: " + port;
    }

    // A test endpoint to identify the instance
    @Value("${spring.application.name:SpringBootApp}")
    private String appName;

    @GetMapping("/instance")
    public String getInstanceInfo() {
        return "Response from " + appName + " on port: " + port;
    }
}

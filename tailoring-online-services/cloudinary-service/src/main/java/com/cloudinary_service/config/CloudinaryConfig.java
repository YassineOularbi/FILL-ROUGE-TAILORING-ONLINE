package com.cloudinary_service.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "djkk8rrlz",
                "api_key", "163948997244588",
                "api_secret", "_clhgnbmmAim3zqxJIToKI6AjRo"
        ));
    }
}

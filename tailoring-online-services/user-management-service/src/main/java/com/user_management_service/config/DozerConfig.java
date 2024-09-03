package com.user_management_service.config;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import com.user_management_service.dto.AdminDto;
import com.user_management_service.dto.CustomerDto;
import com.user_management_service.dto.TailorDto;
import com.user_management_service.dto.UserDto;
import com.user_management_service.model.Admin;
import com.user_management_service.model.Customer;
import com.user_management_service.model.Tailor;
import com.user_management_service.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Configuration
public class DozerConfig {

    @Bean
    public Mapper dozerMapper() {
        return DozerBeanMapperBuilder.buildDefault();
    }

    @Bean
    public BeanMappingBuilder beanMappingBuilder() {
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                // Mapping de base pour User
                mapping(User.class, UserDto.class)
                        .exclude("id")
                        .exclude("address")
                        .exclude("banks");

                // Mapping pour Tailor
                mapping(Tailor.class, TailorDto.class)
                        .exclude("id")
                        .exclude("address")
                        .exclude("banks");

                // Mapping pour Customer
                mapping(Customer.class, CustomerDto.class)
                        .exclude("id")
                        .exclude("address")
                        .exclude("banks")
                        .exclude("shippingAddress");

                // Mapping pour Admin
                mapping(Admin.class, AdminDto.class)
                        .exclude("id")
                        .exclude("address")
                        .exclude("banks");
            }
        };
    }

    public static class PartialUpdateUtil {
        public static void partialUpdate(Object source, Object target) {
            Field[] fields = source.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = ReflectionUtils.getField(field, source);
                if (value != null) {
                    ReflectionUtils.setField(field, target, value);
                }
            }
        }
    }
}

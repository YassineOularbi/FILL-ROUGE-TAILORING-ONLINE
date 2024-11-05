package com.api_gateway_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class DynamicLoadBalancerConfig {

    private static final Logger logger = LoggerFactory.getLogger(DynamicLoadBalancerConfig.class);
    private static final int MAX_REQUESTS_PER_INSTANCE = 50;

    private final DiscoveryClient discoveryClient;

    public DynamicLoadBalancerConfig(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Bean
    @Primary
    public LoadBalancerClient loadBalancerClient() {
        return new DynamicLoadBalancerClient(discoveryClient);
    }

    private static class DynamicLoadBalancerClient implements LoadBalancerClient {
        private final Random random = new Random();
        private final DiscoveryClient discoveryClient;
        private final Map<String, Map<ServiceInstance, AtomicInteger>> instanceLoad = new ConcurrentHashMap<>();

        public DynamicLoadBalancerClient(DiscoveryClient discoveryClient) {
            this.discoveryClient = discoveryClient;
        }

        @Override
        public <T> T execute(String serviceId, LoadBalancerRequest<T> request) {
            logger.info("Executing request for service: {}", serviceId);
            ServiceInstance instance = choose(serviceId);

            if (instance == null || getLoadForInstance(serviceId, instance).get() >= MAX_REQUESTS_PER_INSTANCE) {
                logger.warn("No instances available or too many requests for service: {}", serviceId);
                throw new RuntimeException("No instances available for service: " + serviceId);
            }

            logger.info("Instance chosen: {} with URI: {} for service: {}", instance.getServiceId(), instance.getUri(), serviceId);
            incrementLoadForInstance(serviceId, instance);
            try {
                return request.apply(instance);
            } catch (Exception e) {
                logger.error("Error during request execution for service: {}", serviceId, e);
                throw new RuntimeException(e);
            } finally {
                decrementLoadForInstance(serviceId, instance);
                logger.info("Request completed for service: {}, load decremented for instance: {}", serviceId, instance.getUri());
            }
        }

        @Override
        public <T> T execute(String serviceId, ServiceInstance serviceInstance, LoadBalancerRequest<T> request) {
            logger.info("Executing request for service: {} on instance: {}", serviceId, serviceInstance.getUri());
            incrementLoadForInstance(serviceId, serviceInstance);
            try {
                return request.apply(serviceInstance);
            } catch (Exception e) {
                logger.error("Error during request execution for service: {} on instance: {}", serviceId, serviceInstance.getUri(), e);
                throw new RuntimeException(e);
            } finally {
                decrementLoadForInstance(serviceId, serviceInstance);
                logger.info("Request completed for service: {}, load decremented for instance: {}", serviceId, serviceInstance.getUri());
            }
        }

        @Override
        public URI reconstructURI(ServiceInstance instance, URI original) {
            logger.debug("Reconstructing URI for instance: {} from original URI: {}", instance.getUri(), original);
            return UriComponentsBuilder.fromUri(original)
                    .host(instance.getHost())
                    .port(instance.getPort())
                    .scheme("http")
                    .build()
                    .toUri();
        }

        @Override
        public ServiceInstance choose(String serviceId) {
            logger.info("Choosing an instance for service: {}", serviceId);
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
            if (instances == null || instances.isEmpty()) {
                logger.warn("No instances found for service: {}", serviceId);
                return null;
            }
            ServiceInstance instance = instances.get(random.nextInt(instances.size()));
            logger.info("Instance chosen: {} with URI: {} for service: {}", instance.getServiceId(), instance.getUri(), serviceId);
            return instance;
        }

        @Override
        public <T> ServiceInstance choose(String serviceId, Request<T> request) {
            return choose(serviceId);
        }

        private AtomicInteger getLoadForInstance(String serviceId, ServiceInstance instance) {
            return instanceLoad
                    .computeIfAbsent(serviceId, k -> new ConcurrentHashMap<>())
                    .computeIfAbsent(instance, k -> new AtomicInteger(0));
        }

        private void incrementLoadForInstance(String serviceId, ServiceInstance instance) {
            int newLoad = getLoadForInstance(serviceId, instance).incrementAndGet();
            logger.debug("Load incremented for instance: {} of service: {}. New load: {}", instance.getUri(), serviceId, newLoad);
        }

        private void decrementLoadForInstance(String serviceId, ServiceInstance instance) {
            int newLoad = getLoadForInstance(serviceId, instance).decrementAndGet();
            logger.debug("Load decremented for instance: {} of service: {}. New load: {}", instance.getUri(), serviceId, newLoad);
        }
    }
}

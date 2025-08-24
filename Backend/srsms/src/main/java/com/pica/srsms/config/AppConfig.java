package com.pica.srsms.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


//@Configuration
//public class AppConfig {
//    @Bean
//    @LoadBalanced //not sure if working correctly
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder.build();
//    }
//}

@Configuration
public class AppConfig {

    @Bean
    @LoadBalanced   // working correctly
    public RestTemplate restTemplate() {
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        factory.setConnectTimeout(5000);  // 5 seconds
//        factory.setReadTimeout(5000);
//        return new RestTemplate(factory);

        return new RestTemplate();
    }

}
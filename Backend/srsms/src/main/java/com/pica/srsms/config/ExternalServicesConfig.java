package com.pica.srsms.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ExternalServicesConfig {
//    @Value("${kroki.url}")
//    private String krokiUrl;
//
//    @Value("${aims.url}")
//    private String aimsUrl;
//
//    @Bean
//    public String krokiUrl() {
//        return krokiUrl;
//    }
//
//    @Bean
//    public String aimsUrl() {
//        return aimsUrl;
//    }
//}


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ExternalServicesConfig {

    @Bean
    public RestTemplate externalRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
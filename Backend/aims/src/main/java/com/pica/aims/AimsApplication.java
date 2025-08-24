package com.pica.aims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AimsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AimsApplication.class, args);
	}

}

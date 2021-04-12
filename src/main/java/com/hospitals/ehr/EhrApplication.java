package com.hospitals.ehr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EhrApplication {

	public static void main(String[] args) {
		SpringApplication.run(EhrApplication.class, args);
	}

	@Bean("ehrTemplate")
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}

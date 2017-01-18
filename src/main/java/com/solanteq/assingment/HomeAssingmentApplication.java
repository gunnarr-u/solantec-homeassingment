package com.solanteq.assingment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration(exclude = AppConfig.class)
public class HomeAssingmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeAssingmentApplication.class, args);
	}
}

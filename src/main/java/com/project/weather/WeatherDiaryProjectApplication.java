package com.project.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // spring-boot-starter-data-jpa 사용시 자동 적용
@EnableScheduling
public class WeatherDiaryProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherDiaryProjectApplication.class, args);
	}

}

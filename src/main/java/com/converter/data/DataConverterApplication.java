package com.converter.data;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DataConverterApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataConverterApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		String[] allowDomains = new String[2];
		allowDomains[0] = "http://localhost:4200";
		allowDomains[1] = "http://localhost:8080";

		System.out.println("CORS configuration....");
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins(allowDomains);
			}
		};
	}

	@Bean
	public XSSFWorkbook excelWorkbook() {
		return new XSSFWorkbook();
	}
}

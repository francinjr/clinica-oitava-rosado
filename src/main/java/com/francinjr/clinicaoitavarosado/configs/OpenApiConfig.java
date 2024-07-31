package com.francinjr.clinicaoitavarosado.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
	
	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("Clínica Oitava Rosado")
				.version("v1")
				.description("API para gerenciamento da clínica")
				.contact(
						new Contact()
						.name("Francinaldo Manoel dos Anjos Junior")
						.email("francinaldomanoel135@gmail.com")
						.url("https://github.com/francinjr")
						)
				.termsOfService("Termos")
				.license(
					new License()
						.name("Apache 2.0")
						.url("URL")
					)
				);
	}

}

package com.binhnd.pmsbe;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.DecimalFormat;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "${swagger.config.title}",
		description = "${swagger.config.title}",
		version = "${swagger.config.version}"),
		servers = {@Server(url = "${swagger.config.url}")})
public class PmsBeApplication {
	private static final Logger L = LoggerFactory.getLogger(PmsBeApplication.class);
	public static void main(String[] args) {

		System.setProperty("spring.devtools.restart.enabled", "true");
		System.setProperty("java.net.preferIPv4Stack", "true");

		// 1. Run spring application
		SpringApplication.run(PmsBeApplication.class, args);

		L.info("-----------------------------------------------------------");
		L.info("            Welcome to New PMS Service.");
		L.info("           Application start successfully.");
		L.info("-----------------------------------------------------------");

	}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DecimalFormat decimalFormat() {
		return new DecimalFormat("#.##");
	}

}

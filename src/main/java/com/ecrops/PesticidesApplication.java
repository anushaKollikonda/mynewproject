package com.ecrops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import com.ecrops.config.CustomPasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = "com.ecrops")
public class PesticidesApplication extends SpringBootServletInitializer{
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(PesticidesApplication.class);
		
		
	}

	public static void main(String[] args) {
		SpringApplication.run(PesticidesApplication.class, args);
	}

}

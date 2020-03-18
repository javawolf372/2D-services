package com.zhixin.wolf.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.SpringServletContainerInitializer;

@ComponentScan(value = "com.zhixin.wolf")
@SpringBootApplication
public class Map2DServicesApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Map2DServicesApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(Map2DServicesApplication.class, args);
	}

}

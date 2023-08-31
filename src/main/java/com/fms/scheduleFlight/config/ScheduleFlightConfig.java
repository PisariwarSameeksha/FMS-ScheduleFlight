package com.fms.scheduleFlight.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ScheduleFlightConfig {
	@Bean
	public ModelMapper modelMapper() {
		
		return new ModelMapper();
	}

	@Bean
	public WebClient webclient(){
		return WebClient.builder().build();
	}
}

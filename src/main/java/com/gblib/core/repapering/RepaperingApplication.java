package com.gblib.core.repapering;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
public class RepaperingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepaperingApplication.class, args);
	}
	
	@Bean
	public CorsFilter corsFilter() {
	
		final UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
		config.setAllowedHeaders(Arrays.asList("Origin","Content-Type","Accept"));
		config.setAllowedMethods(Arrays.asList("GET","POST","PUT","OPTIONS","DELETE","PATCH"));
		source.registerCorsConfiguration("/**",config);
		return new CorsFilter(source);
	}
}

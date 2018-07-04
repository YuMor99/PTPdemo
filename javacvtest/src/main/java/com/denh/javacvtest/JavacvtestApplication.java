package com.denh.javacvtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JavacvtestApplication {

	public static void main(String[] args) {
//		SpringApplicationBuilder builder = new SpringApplicationBuilder(JavacvtestApplication.class);
//		builder.headless(false).web(false).run(args);
		System.setProperty("java.awt.headless","false");
		SpringApplication.run(JavacvtestApplication.class, args);
	}
}

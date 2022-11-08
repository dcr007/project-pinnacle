package com.ondemand.tools.perflog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableAutoConfiguration
public class PerflogParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(PerflogParserApplication.class, args);
	}

}

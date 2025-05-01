package it.codingjam.springbootgrpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootGrpcApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootGrpcApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootGrpcApplication.class, args);
		LOGGER.info("Available processors: {}", Runtime.getRuntime().availableProcessors());
	}

}

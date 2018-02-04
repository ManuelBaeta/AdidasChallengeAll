package com.manolo.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Starter class: Spring boot entry point.
 * It also defined top of hierarchy for all classed being scanned by Spring.
 * It also defines the application as a Eureka Client (Service Registry).
 * 
 */
@SpringBootApplication
@EnableEurekaClient
public class Application {

	private final static Logger log = LoggerFactory.getLogger(Application.class);
    
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
        log.info("Starting app...");
        SpringApplication.run(Application.class, args);
	}
}

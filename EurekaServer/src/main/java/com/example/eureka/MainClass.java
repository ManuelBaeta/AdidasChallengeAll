package com.example.eureka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Starter class: Spring boot entry point.
 * 
 * It acts as an Eureka Server (@EnableEurekaServer) in standalone mode. It is based on Netflix implementation of Service Registry pattern.
 *   Eureka clients can register with this server. Clients will receive periodically updates with changes on Eureka Server registries.
 * It is able to register itself with an Eureka Server. Once it is register it receives from Eureka periodically 
 *  updates about the microservice instances running. It requires specific configuration on application.yml: eureka.client properties. 
 * 
 */
@SpringBootApplication
@EnableEurekaServer
public class MainClass {
	
	private final static Logger log = LoggerFactory.getLogger(MainClass.class);
    
    public static void main(String []args) {
        log.info("Starting app...");
        SpringApplication.run(MainClass.class, args);
    }
	
}

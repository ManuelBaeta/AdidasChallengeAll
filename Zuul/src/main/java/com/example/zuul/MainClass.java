package com.example.zuul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


/**
 * Starter class: Spring boot entry point.
 * 
 * It acts as an Eureka client (@EnableEurekaClient): It is able to register itself with an Eureka Server. Once it is register it receives from Eureka periodically 
 *  updates about the microservice instances running. It requires specific configuration on application.yml: eureka.client properties. 
 * 
 * It acts as a Zuul proxy(@EnableZuulProxy): It acts as a router/load balancer:  
 *  proxy uses Ribbon to locate an instance to forward to via discovery, and all requests are executed in a hystrix command, 
 * 
 * It also defines the init of the hierarchy of all classed being scanned by Spring (labeled as @SprinBootApplication)
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class MainClass {
	
	private final static Logger log = LoggerFactory.getLogger(MainClass.class);
    
    public static void main(String []args) {
        log.info("Starting app...");
        SpringApplication.run(MainClass.class, args);
    }
    
}

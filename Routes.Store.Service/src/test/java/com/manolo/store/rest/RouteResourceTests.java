package com.manolo.store.rest;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.manolo.store.domain.Route;
import com.manolo.store.service.JsonException;


@ActiveProfiles("testing")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RouteResourceTests {
	private final static Logger log = LoggerFactory.getLogger(RouteResourceTests.class);
	
	Route route = null;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	private HttpEntity<String> requestWithRoutePayload;
	
	private HttpEntity<String> requestWithoutPayload;
	
	@Before
	public void setup() throws JsonException {
		route = new Route("Zaragoza", "Barcelona", new Date(), new Date());
		
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        requestWithRoutePayload =  new HttpEntity<>(route.toJson(), headers);
        requestWithoutPayload =  new HttpEntity<>(headers);
        
        _clearDB();
	}
	
	private void _clearDB() {
		//Delete all routes ...
		this.restTemplate.exchange("/routes-store-service/api/route", HttpMethod.DELETE, requestWithoutPayload, String.class);
	}
	
	@Test
	public void addRouteOK()  {
		log.info("Add new Route.");
		
		ResponseEntity<String> entity = this.restTemplate.exchange("/routes-store-service/api/route", HttpMethod.POST, requestWithRoutePayload, String.class);
		assertTrue(entity.getStatusCode().is2xxSuccessful());
	}
	
	@Test
	public void addRouteAlreadyPresent() {
		log.info("Try to add an already present route.");
		
		//Store route...
		ResponseEntity<String> entity = this.restTemplate.exchange("/routes-store-service/api/route", HttpMethod.POST, requestWithRoutePayload, String.class);
		assertTrue(entity.getStatusCode().is2xxSuccessful());
		
		//Try to store again the same route...
		entity = this.restTemplate.exchange("/routes-store-service/api/route", HttpMethod.POST, requestWithRoutePayload, String.class);
		assertTrue(entity.getStatusCode().is4xxClientError());
	}
	
	@Test
	public void getRoutes() {
		log.info("Get all routes.");
		
		//Store route
		ResponseEntity<String> entity = this.restTemplate.exchange("/routes-store-service/api/route", HttpMethod.POST, requestWithRoutePayload, String.class);
		assertTrue(entity.getStatusCode().is2xxSuccessful());
		
		//get all routes ...
		entity = this.restTemplate.exchange("/routes-store-service/api/route", HttpMethod.GET, requestWithoutPayload, String.class);
		assertTrue(entity.getStatusCode().is2xxSuccessful());
	}
	
	@Test
	public void getRoutesNoRoutes() {
		log.info("Get all routes: No routes present.");
		
		//get all routes ... no routes present..
		ResponseEntity<String> entity = this.restTemplate.exchange("/routes-store-service/api/route", HttpMethod.GET, requestWithoutPayload, String.class);
		assertTrue(entity.getStatusCode().is4xxClientError());
	}
	
	@Test
	public void getRoute() {
		log.info("Get a specific route.");
		
		//Store route
		ResponseEntity<String> entity = this.restTemplate.exchange("/routes-store-service/api/route", HttpMethod.POST, requestWithRoutePayload, String.class);
		assertTrue(entity.getStatusCode().is2xxSuccessful());
		
		Route route = null;
		try {
			route = Route.fromJson(entity.getBody());
		} catch (JsonException e) {
			Assert.fail("Unexpected. An exception should happen");
		}
		
		//get specific .. previous route ..
		entity = this.restTemplate.exchange("/routes-store-service/api/route/{origenId}", HttpMethod.GET, requestWithoutPayload, String.class, route.getId());
		assertTrue(entity.getStatusCode().is2xxSuccessful());
	}
	
	@Test
	public void getRouteNoRoutes() {
		log.info("Get a specific route: That route is not present.");
		
		//Get specific route .. but there is no such route
		ResponseEntity<String> entity = this.restTemplate.exchange("/routes-store-service/api/route/{origenId}", HttpMethod.GET, requestWithoutPayload, String.class, 100);
		assertTrue(entity.getStatusCode().is4xxClientError());
	}
	
	@Test
	public void deleteRoutes() {
		log.info("Delete all routes");
		
		//Store route
		ResponseEntity<String> entity = this.restTemplate.exchange("/routes-store-service/api/route", HttpMethod.POST, requestWithRoutePayload, String.class);
		assertTrue(entity.getStatusCode().is2xxSuccessful());
		
		//Delete all routes ...
		this.restTemplate.exchange("/routes-store-service/api/route", HttpMethod.DELETE, requestWithoutPayload, String.class);
		assertTrue(entity.getStatusCode().is2xxSuccessful());
	}
	
	@Test
	public void deleteRoutesNoRoutes() {
		log.info("Delete all routes. No routes present");
		
		//Delete all routes .. but there are no routes ...
		ResponseEntity<String> entity = this.restTemplate.exchange("/routes-store-service/api/route", HttpMethod.DELETE, requestWithoutPayload, String.class);
		assertTrue(entity.getStatusCode().is2xxSuccessful());		
	}
	
	@Test
	public void deleteRoute() {
		log.info("Delete a specific route.");
		
		// Store a route ..
		ResponseEntity<String> entity = this.restTemplate.exchange("/routes-store-service/api/route", HttpMethod.POST, requestWithRoutePayload, String.class);
		assertTrue(entity.getStatusCode().is2xxSuccessful());		
		
		Route route = null;
		try {
			route = Route.fromJson(entity.getBody());
		} catch (JsonException e) {
			Assert.fail("Unexpected. An exception should happen");
		}
		
		// Delete previous route ..
		entity = this.restTemplate.exchange("/routes-store-service/api/route/{origenId}", HttpMethod.DELETE, requestWithoutPayload, String.class, route.getId());
		assertTrue(entity.getStatusCode().is2xxSuccessful());
	}
	
	@Test
	public void deleteRouteNoRoute() {
		log.info("Delete a specific route: That route is not present.");
		
		//Delete specific route .. there is no such route ...
		ResponseEntity<String> entity = this.restTemplate.exchange("/routes-store-service/api/route/{origenId}", HttpMethod.DELETE, requestWithoutPayload, String.class, 100);
		assertTrue(entity.getStatusCode().is4xxClientError());
	}
	
}

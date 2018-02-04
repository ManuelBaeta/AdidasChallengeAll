package com.manolo.optimizer.rest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.manolo.optimizer.domain.service.GetOptimizedRouteRequest;
import com.manolo.optimizer.domain.service.GetOptimizedRouteResponse;
import com.manolo.optimizer.domain.service.OptimizedItinerary;
import com.manolo.optimizer.service.BusinessExceptionCause;
import com.manolo.optimizer.service.RouteOptimizerService;
import com.manolo.optimizer.service.RouteOptimizerServiceException;

@ActiveProfiles("testing")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OptmizedRouteResourceTests {

    private final static Logger log = LoggerFactory.getLogger(OptmizedRouteResourceTests.class);
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	OptimizedRouteResource resource;
    
	@MockBean
    private RouteOptimizerService routeOptimizeService;
	   
    private List<List<String>> getItineraryList(String ... cities) {
    	List<List<String>> listOfItineraries = new ArrayList<>();
    	listOfItineraries.add(Arrays.asList(cities));
    	return listOfItineraries;
    }
    
	@Before
	public void setup() throws RouteOptimizerServiceException {
        log.info("Setup expectedOptItineraries");
        Collection<OptimizedItinerary> expectedOptItinerariesByTime = new ArrayList<>(); 
        Collection<OptimizedItinerary> expectedOptItinerariesByHops = new ArrayList<>();
        
        expectedOptItinerariesByTime.add(new OptimizedItinerary.Builder(getItineraryList("Zaragoza", "Barcelona")).from("Zaragoza").to("Barcelona").setCost(3.0).build());
        expectedOptItinerariesByTime.add(new OptimizedItinerary.Builder(getItineraryList("Zaragoza", "Barcelona", "Guadalajara")).from("Zaragoza").to("Guadalajara").setCost(4.0).build());
        expectedOptItinerariesByTime.add(new OptimizedItinerary.Builder(getItineraryList("Zaragoza", "Barcelona", "Guadalajara", "Madrid")).from("Zaragoza").to("Madrid").setCost(5.0).build());
        
        expectedOptItinerariesByHops.add(new OptimizedItinerary.Builder(getItineraryList("Zaragoza", "Barcelona")).from("Zaragoza").to("Barcelona").setCost(1.0).build());
        expectedOptItinerariesByHops.add(new OptimizedItinerary.Builder(getItineraryList("Zaragoza", "Barcelona", "Guadalajara")).from("Zaragoza").to("Guadalajara").setCost(2.0).build());
        expectedOptItinerariesByHops.add(new OptimizedItinerary.Builder(getItineraryList("Zaragoza", "Barcelona", "Madrid")).from("Zaragoza").to("Madrid").setCost(2.0).build());
        
		log.info("Mocking routeOptimizeService to return OK when calling for Zaragoza");
		when(routeOptimizeService.getOptimizedRoutes( new GetOptimizedRouteRequest("Zaragoza")))
			.thenReturn(new GetOptimizedRouteResponse(expectedOptItinerariesByTime, expectedOptItinerariesByHops));
		
		log.info("Mocking routeOptimizeService to return exception when calling for barbate");
		when(routeOptimizeService.getOptimizedRoutes( new GetOptimizedRouteRequest("barbate")))
			.thenThrow(new RouteOptimizerServiceException("city not found", BusinessExceptionCause.ORIGIN_CITY_NOT_FOUND));		
	}
	
    @Test
    public void noCity() {
    	log.info("Calling optimized itineraries without passing a city:");
    	ResponseEntity<String> entity = this.restTemplate.getForEntity("/routes-optimizer-service/api/route?origin=", String.class );
    	assertTrue(entity.getStatusCode().is4xxClientError());
    }
	
    @Test
    public void unknownCity() {
    	log.info("Calling optimized itineraries passing a unknown city: barbate");
    	ResponseEntity<String> entity = this.restTemplate.getForEntity("/routes-optimizer-service/api/route?origin=barbate", String.class );
    	assertTrue(entity.getStatusCode().is4xxClientError());
    }
    
    @Test
    public void knownCity() {
    	log.info("Calling optimized itineraries passing a valid city: Zaragoza");
    	ResponseEntity<String> entity = this.restTemplate.getForEntity("/routes-optimizer-service/api/route?origin=Zaragoza", String.class );
    	assertTrue(entity.getStatusCode().is2xxSuccessful());
    }
	
}

package com.manolo.optimizer.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manolo.optimizer.domain.service.OptimizedItinerary;
import com.manolo.optimizer.domain.service.RouteNode;
import com.manolo.optimizer.service.strategy.DijkstraOptimizedItineraryStrategy;
import com.manolo.optimizer.service.strategy.OptimizedItineraryContext;
import com.manolo.optimizer.service.strategy.OptimizedItineraryStrategyException;

@RunWith(MockitoJUnitRunner.class)
public class StrategyTests {

    private final static Logger log = LoggerFactory.getLogger(StrategyTests.class);
    
    private Collection<RouteNode> routeNodes = new ArrayList<>();
    
    private Collection<OptimizedItinerary> expectedOptItinerariesByTime = new ArrayList<>();
    
    private Collection<OptimizedItinerary> expectedOptItinerariesByHops = new ArrayList<>();
    
    @Before
    public void setup() {
    	log.info("Setup routeNodes");
    	routeNodes.add(new RouteNode("Madrid", "Zaragoza", 3));
    	routeNodes.add(new RouteNode("Madrid", "Guadalajara", 1));
    	routeNodes.add(new RouteNode("Zaragoza", "Barcelona", 3));
    	routeNodes.add(new RouteNode("Guadalajara", "Zaragoza", 1));
        routeNodes.add(new RouteNode("Guadalajara", "Madrid", 1));
        routeNodes.add(new RouteNode("Barcelona", "Guadalajara", 1));
        routeNodes.add(new RouteNode("Barcelona", "Madrid", 3));
        log.info("routeNodes: " + routeNodes);
            
        log.info("Setup expectedOptItinerariesByTime");
        expectedOptItinerariesByTime.add(new OptimizedItinerary.Builder(getItineraryList("Zaragoza", "Barcelona")).from("Zaragoza").to("Barcelona").setCost(3.0).build());
        expectedOptItinerariesByTime.add(new OptimizedItinerary.Builder(getItineraryList("Zaragoza", "Barcelona", "Guadalajara")).from("Zaragoza").to("Guadalajara").setCost(4.0).build());
        expectedOptItinerariesByTime.add(new OptimizedItinerary.Builder(getItineraryList("Zaragoza", "Barcelona", "Guadalajara", "Madrid")).from("Zaragoza").to("Madrid").setCost(5.0).build());
        log.info("expectedOptItinerariesByTime: " +  expectedOptItinerariesByTime);
        
        log.info("Setup expectedOptItinerariesByHops");
        expectedOptItinerariesByHops.add(new OptimizedItinerary.Builder(getItineraryList("Zaragoza", "Barcelona")).from("Zaragoza").to("Barcelona").setCost(1.0).build());
        expectedOptItinerariesByHops.add(new OptimizedItinerary.Builder(getItineraryList("Zaragoza", "Barcelona", "Guadalajara")).from("Zaragoza").to("Guadalajara").setCost(2.0).build());
        expectedOptItinerariesByHops.add(new OptimizedItinerary.Builder(getItineraryList("Zaragoza", "Barcelona", "Madrid")).from("Zaragoza").to("Madrid").setCost(2.0).build());
        log.info("expectedOptItinerariesByHops:" + expectedOptItinerariesByHops);
    }
    
    private List<List<String>> getItineraryList(String ... cities) {
    	List<List<String>> listOfItineraries = new ArrayList<>();
    	listOfItineraries.add(Arrays.asList(cities));
    	return listOfItineraries;
    }
    
    @Test
    public void testAlgorithmUnknownOrigin() {
    	String originCity= "Barbate";
        OptimizedItineraryContext itineraryContext = new OptimizedItineraryContext( new DijkstraOptimizedItineraryStrategy());
		
		try {
			itineraryContext.getBestItineraryByTime(originCity, routeNodes);
			itineraryContext.getBestItineraryByHops(originCity, routeNodes);
			
			Assert.fail("A exception should be raised when a unknown city is provided.");
		} catch (OptimizedItineraryStrategyException e) {
			log.info("Test Passed: A exception is raised when a unknown city is provided.");
		}
    	
    }
    
    @Test
    public void testAlgorithmKnownOrigin() {
    	String originCity= "Zaragoza";
        OptimizedItineraryContext itineraryContext = new OptimizedItineraryContext( new DijkstraOptimizedItineraryStrategy());
        
        Collection<OptimizedItinerary> realOptItinerariesByTime;
        Collection<OptimizedItinerary> realOptItinerariesByHops;
		try {
			realOptItinerariesByTime = itineraryContext.getBestItineraryByTime(originCity, routeNodes);
			realOptItinerariesByHops  = itineraryContext.getBestItineraryByHops(originCity, routeNodes);
			
			realOptItinerariesByTime
				.forEach( realItinerararies -> {
					expectedOptItinerariesByTime.forEach(expectedItineraries -> {
						if (realItinerararies.getDestination().equals(expectedItineraries.getDestination())) {
							Assert.assertEquals("For destination: " + expectedItineraries.getDestination() + " real and expected costs are equal"
									, expectedItineraries.getCost(), realItinerararies.getCost());
						}
					});
			});
			
			realOptItinerariesByHops
				.forEach( realItinerararies -> {
					expectedOptItinerariesByHops.forEach(expectedItineraries -> {
						if (realItinerararies.getDestination().equals(expectedItineraries.getDestination())) {
							Assert.assertEquals("For destination: " + expectedItineraries.getDestination() + " real and expected costs are equal"
								, expectedItineraries.getCost(), realItinerararies.getCost());
						}
					});
		});
			
		} catch (OptimizedItineraryStrategyException e) {
			Assert.fail("Unexpected exception. Msg: " + e);
		}


    	
    }
}

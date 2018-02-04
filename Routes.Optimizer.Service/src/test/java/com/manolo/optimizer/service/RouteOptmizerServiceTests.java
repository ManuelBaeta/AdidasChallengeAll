package com.manolo.optimizer.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import com.manolo.optimizer.domain.BaseRestMsg;
import com.manolo.optimizer.domain.Route;
import com.manolo.optimizer.domain.service.GetOptimizedRouteRequest;


@RunWith(SpringRunner.class)
@RestClientTest(RouteOptimizerService.class)
public class RouteOptmizerServiceTests {

	private final static Logger log = LoggerFactory.getLogger(RouteOptmizerServiceTests.class);
	
    @Autowired
    private MockRestServiceServer server;
    
	@MockBean
    private LoadBalancerClient loadBalancer;
	
	@Autowired
	private RouteOptimizerService service;
    	
	Collection<Route> allRoutes = new ArrayList<>();
	
    @Before
    public void setup() {     
        log.info("Setup some Routes ...");
        allRoutes.add(new Route("Zaragoza", "Barcelona", new Date(), new Date()));
        allRoutes.add(new Route("Barcelona", "Zaragoza", new Date(), new Date()));
        allRoutes.add(new Route("Madrid", "Barcelona", new Date(), new Date()));
        
    	log.info("Mocking ServiceInstance ...");
		ServiceInstance instance = mock(ServiceInstance.class);
		when(instance.getHost()).thenReturn("localhost");
		when(instance.getPort()).thenReturn(9000);
		
		log.info("Mocking loadBalancer ...");
		when(loadBalancer.choose("routes-store-service")).thenReturn(instance);
		
    }
    
    @Test
    public void backendResponsesServiceUnavailable() {
    	log.info("Calling optimized itineraries. Backend responses with Service Unavailable");
        this.server.expect(requestTo("http://localhost:9000/routes-store-service/api/route"))
			.andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));	
        
        try {
			service.getOptimizedRoutes(new GetOptimizedRouteRequest("Zaragoza"));
			Assert.fail("Unexpected. An exception should happen");
			
		} catch (RouteOptimizerServiceException e) {
			Assert.assertEquals(BusinessExceptionCause.ROUTE_STORE_SERVICE_UNAVAILABLE, e.getBusinessCause() );
		}
    }
    
    @Test
    public void backendResponsesUnexpectedErrorCode() {
    	log.info("Calling optimized itineraries. Backend responses with unexpected error code");
        this.server.expect(requestTo("http://localhost:9000/routes-store-service/api/route"))
			.andRespond(withStatus(HttpStatus.BAD_REQUEST));	
        
        try {
			service.getOptimizedRoutes(new GetOptimizedRouteRequest("Zaragoza"));
			Assert.fail("Unexpected. An exception should happen");
			
		} catch (RouteOptimizerServiceException e) {
			Assert.assertEquals(BusinessExceptionCause.ROUTE_STORE_SERVICE_RESPONSE_WITH_ERRORS, e.getBusinessCause() );
		}
    }
    
    @Test
    public void backendHasNoRoutes() {
    	log.info("Calling optimized itineraries. Backend has no routes");
        this.server.expect(requestTo("http://localhost:9000/routes-store-service/api/route"))
			.andRespond(withStatus(HttpStatus.NOT_FOUND));
        
        try {
			service.getOptimizedRoutes(new GetOptimizedRouteRequest("Zaragoza"));
			Assert.fail("Unexpected. An exception should happen");
			
		} catch (RouteOptimizerServiceException e) {
			Assert.assertEquals(BusinessExceptionCause.ROUTE_STORE_SERVICE_NO_ROUTES, e.getBusinessCause() );
		}
    }
    
    @Test
    public void originCityIsUnknown() throws JsonException {
        this.server.expect(requestTo("http://localhost:9000/routes-store-service/api/route"))
			.andRespond(withSuccess(BaseRestMsg.toJsonCollection(allRoutes), MediaType.APPLICATION_JSON));
        
        try {
			service.getOptimizedRoutes(new GetOptimizedRouteRequest("barbate"));
			Assert.fail("Unexpected. An exception should happen");
			
		} catch (RouteOptimizerServiceException e) {
			Assert.assertEquals(BusinessExceptionCause.ORIGIN_CITY_NOT_FOUND, e.getBusinessCause() );
		}
    }
    
    @Test
    public void originCityIsKnown() throws JsonException {
        this.server.expect(requestTo("http://localhost:9000/routes-store-service/api/route"))
			.andRespond(withSuccess(BaseRestMsg.toJsonCollection(allRoutes), MediaType.APPLICATION_JSON));
        
        try {
			service.getOptimizedRoutes(new GetOptimizedRouteRequest("Zaragoza"));
			
		} catch (RouteOptimizerServiceException e) {
			Assert.fail("Unexpected. An exception should not happen. Msg:" + e);
		}
    }
	
}

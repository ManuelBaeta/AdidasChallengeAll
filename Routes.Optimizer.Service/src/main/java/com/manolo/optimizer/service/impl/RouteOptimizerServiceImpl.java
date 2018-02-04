package com.manolo.optimizer.service.impl;

import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.manolo.optimizer.domain.Route;
import com.manolo.optimizer.domain.service.GetOptimizedRouteRequest;
import com.manolo.optimizer.domain.service.GetOptimizedRouteResponse;
import com.manolo.optimizer.domain.service.OptimizedItinerary;
import com.manolo.optimizer.domain.service.RouteNode;
import com.manolo.optimizer.service.BusinessExceptionCause;
import com.manolo.optimizer.service.JsonException;
import com.manolo.optimizer.service.RouteOptimizerService;
import com.manolo.optimizer.service.RouteOptimizerServiceException;
import com.manolo.optimizer.service.strategy.DijkstraOptimizedItineraryStrategy;
import com.manolo.optimizer.service.strategy.OptimizedItineraryContext;
import com.manolo.optimizer.service.strategy.OptimizedItineraryStrategyException;

/**
 * The Class RouteOptimizerServiceImpl provides a basic implementation of RouteOptimizerService.
 * It provides following features:
 *    Load balanced REST calls acting as a Ribbon Client.
 *    Use of different strategies (OptimizedItineraryStrategy) to get optimized routes
 *
 */
@Service
public class RouteOptimizerServiceImpl implements  RouteOptimizerService {

    private final Logger log = LoggerFactory.getLogger(RouteOptimizerServiceImpl.class);
    
    @Autowired
    private LoadBalancerClient loadBalancer;
    
    private final RestTemplate restTemplate;
    
    private final String AUTHORIZATION = "Authorization";
    
    public RouteOptimizerServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }
    
    /**
     * Gets the optimized routes.
     *
     * @param request the request
     * @return the optimized routes
     * @throws RouteOptimizerServiceException the route optimizer service exception
     * @see com.manolo.optimizer.service.RouteOptimizerService#getOptimizedRoutes(com.manolo.optimizer.domain.service.GetOptimizedRouteRequest)
     */
    @Override
    public GetOptimizedRouteResponse getOptimizedRoutes(GetOptimizedRouteRequest request) throws RouteOptimizerServiceException {
        
        String loadBalancerUrl = getServiceUrl();
        //log.info("[OrderRepositoryImpl] Got load balanced Service instance for zuul-edge-router, URL: " + loadBalancerUrl + ", from Eureka Server.");
        log.info("Got load balanced Service instance for routes-store-service, URL: " + loadBalancerUrl + ", from Eureka Server.");
        
        //get required routes from routes-store-service
        Collection<RouteNode> routeNodes = queryRoutes(loadBalancerUrl, request);
        

        //do algorithm to get optmized itineraries, it follows strategy pattern:
        //create strategy context with a specific strategy  and then use the it
        OptimizedItineraryContext itineraryContext = new OptimizedItineraryContext( new DijkstraOptimizedItineraryStrategy());
		try {
			Collection<OptimizedItinerary> optItinerariesByTime = itineraryContext.getBestItineraryByTime(request.getOriginCity(), routeNodes);
			Collection<OptimizedItinerary> optItinerariesByHops  = itineraryContext.getBestItineraryByHops(request.getOriginCity(), routeNodes);
	        return new GetOptimizedRouteResponse(optItinerariesByTime, optItinerariesByHops);
	        
		} catch (OptimizedItineraryStrategyException e) {
			log.error(String.format("Error on strategy. Msg: %s . ", e));
			throw new RouteOptimizerServiceException(e.getMessage(), BusinessExceptionCause.ORIGIN_CITY_NOT_FOUND);
		}
    }

    
    /**
     * Get url of remote service
     * @return
     */
    private String getServiceUrl() {
        //ServiceInstance instance = loadBalancer.choose("zuul-edge-router");
        ServiceInstance instance = loadBalancer.choose("routes-store-service");
        URI loadBalancerUri = URI.create(String.format("http://%s:%s", instance.getHost(), instance.getPort()));
        return loadBalancerUri.toString();
    }
    
    /**
     * Query remote server for routes
     * @param loadBalancerUrl
     * @return
     * @throws RouteOptimizerServiceException
     */
    private Collection<RouteNode> queryRoutes(String loadBalancerUrl, GetOptimizedRouteRequest routeRequest) throws RouteOptimizerServiceException {
        log.info("Getting routes from Routes.Store.Service.");
        
        String errMsg;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        if (!Strings.isNullOrEmpty(routeRequest.getAuthorizationHeader())) {
        	log.info("Adding Authorization header to request towards routes-store-service backend.");
        	headers.add(AUTHORIZATION, routeRequest.getAuthorizationHeader());
        }
        HttpEntity<String> request = new HttpEntity<>(headers);
        
        String url = String.format("%s/routes-store-service/api/route", loadBalancerUrl);
        log.info("->Sending GET request to loadbalancer on Url: " + url );
        
        ResponseEntity<String> entity;
        try {
        	entity = restTemplate.exchange( url, HttpMethod.GET , request , String.class); // could throw RestClientException
            log.info("<-Received: " + entity.getStatusCode() + ", payload: " + entity.getBody());
            
        } catch (ResourceAccessException ex) {
        	//Probably backend is not available ...
        	errMsg = String.format("ResourceAccessException(I/O). is Backend available?? Msg: %s", ex);
        	log.error(errMsg);
        	throw new RouteOptimizerServiceException(errMsg, BusinessExceptionCause.ROUTE_STORE_SERVICE_UNAVAILABLE);
        	
        } catch (RestClientResponseException ex) {
        	errMsg = String.format("RestClientResponseException. Backend returned status code. Msg: %s", ex);
        	log.error(errMsg);

        	if (ex instanceof HttpStatusCodeException) {
        		HttpStatusCodeException e = (HttpStatusCodeException) ex;
        		if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        			//404 is a valid and expected error code .. have a valid businessCause
        			throw new RouteOptimizerServiceException(errMsg, BusinessExceptionCause.ROUTE_STORE_SERVICE_NO_ROUTES);
        			
        		} else if (e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
        			//503 is a valid and expected error code .. have a valid businessCause
        			throw new RouteOptimizerServiceException(errMsg, BusinessExceptionCause.ROUTE_STORE_SERVICE_UNAVAILABLE);
        		}
        	}  
        	throw new RouteOptimizerServiceException(errMsg, BusinessExceptionCause.ROUTE_STORE_SERVICE_RESPONSE_WITH_ERRORS);
        }
 
        try {
            Collection<Route> routes =  Route.fromJsonCollection(entity.getBody());
            
            //Transfor a collection of Route into a collection of RouteNode (that can be used by the strategy).
            Collection<RouteNode> routeNodes = routes.stream()
                    .map(route -> new RouteNode(route))
                    .collect(Collectors.toList());
                
            return routeNodes;
            
        } catch (JsonException e) {
            errMsg = String.format("Error marshalling routes-store-service response. Msg:", e);
            throw new RouteOptimizerServiceException(errMsg, BusinessExceptionCause.ROUTE_STORE_SERVICE_BAD_RESPONSE);
        }
    }
}

package com.manolo.optimizer.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.manolo.optimizer.domain.service.GetOptimizedRouteRequest;
import com.manolo.optimizer.domain.service.GetOptimizedRouteResponse;
import com.manolo.optimizer.service.JsonException;
import com.manolo.optimizer.service.RouteOptimizerService;
import com.manolo.optimizer.service.RouteOptimizerServiceException;


/**
 * OptimizedRouteResource provides a REST endpoint based on JAX-RS to get optimized itineraries from an origin city.
 * 
 *
 */
@Component
@Path("/route")
public class OptimizedRouteResource {

    private final Logger log = LoggerFactory.getLogger(OptimizedRouteResource.class);
    
    @Autowired
    private RouteOptimizerService routeOptimizeService;
    
    @Context
    protected HttpServletRequest req;
    
    private final String AUTHORIZATION = "Authorization";
    
    /**
     * Gets the all routes.
     *
     * @param origin the origin
     * @return the all routes
     * @throws RouteOptimizerServiceException the route optimizer service exception
     * @throws JsonException the json exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoutes(@QueryParam("origin") String origin) throws RouteOptimizerServiceException, JsonException{
        log.info("Get optimized routes from origin city: " + origin);
        
        if (Strings.isNullOrEmpty(origin)) {
            log.error("No origin city was provided as parameter");
            return Response.status(Status.NOT_FOUND).build();
        }
        
        GetOptimizedRouteRequest request = buildGetOptimizedRouteRequest(origin);
        
        GetOptimizedRouteResponse response = routeOptimizeService.getOptimizedRoutes(request);
        log.info("Got this response: " +  response.toJson());
        
        return Response.ok(response.toJson()).build();

    }
    
    private GetOptimizedRouteRequest buildGetOptimizedRouteRequest(String origin) {
    	return Strings.isNullOrEmpty(req.getHeader(AUTHORIZATION)) ? 
    				new GetOptimizedRouteRequest(origin) : new GetOptimizedRouteRequest(origin , req.getHeader(AUTHORIZATION)); 
    			
    }
}

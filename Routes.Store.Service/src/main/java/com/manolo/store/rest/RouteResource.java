package com.manolo.store.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.manolo.store.domain.BaseRestMsg;
import com.manolo.store.domain.Route;
import com.manolo.store.domain.service.AddRouteRequest;
import com.manolo.store.domain.service.DeleteRouteRequest;
import com.manolo.store.domain.service.GetRouteRequest;
import com.manolo.store.domain.service.GetRouteResponse;
import com.manolo.store.service.JsonException;
import com.manolo.store.service.RouteStoreService;
import com.manolo.store.service.RouteStoreServiceException;

/**
 * Set of Rest endpoint based on JAX-RS
 * Provides operations such:
 *    GetRouteById, getAllRoutes, addRoute, deleteRouteById, deleteAllRoutes
 *
 */
@Component
@Path("/route")
public class RouteResource {

    private final Logger log = LoggerFactory.getLogger(RouteResource.class);
    
    /** The store route service. */
    @Autowired
    RouteStoreService storeRouteService;
    
    /**
     * Gets the route by id.
     *
     * @param id the id
     * @return the route by id
     * @throws JsonException the json exception
     * @throws RouteStoreServiceException the route store service exception
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRouteById(@PathParam("id") String id) throws JsonException, RouteStoreServiceException   {
        log.info("Get route by id: " + id);
        
        GetRouteResponse getRouteResponse =  storeRouteService.getRouteById(new GetRouteRequest(id));
        return Response.ok(BaseRestMsg.toJsonCollection(getRouteResponse.getRoutes())).build();
    }
    
    /**
     * Gets the all routes.
     *
     * @return the all routes
     * @throws JsonException the json exception
     * @throws RouteStoreServiceException the route store service exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoutes() throws JsonException, RouteStoreServiceException {
        log.info("Get all routes");
        
        GetRouteResponse getRouteResponse = storeRouteService.getAllRoutes(); 
        
        return Response.ok(BaseRestMsg.toJsonCollection(getRouteResponse.getRoutes())).build();
    }
    
    
    /**
     * Adds the route.
     *
     * @param json the json
     * @return the response
     * @throws JsonException the json exception
     * @throws RouteStoreServiceException the route store service exception
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRoute(String json) throws JsonException, RouteStoreServiceException {
        log.info("Add new route. Payload: " + json);
        
        Route route = Route.fromJson(json);
        AddRouteRequest request = new AddRouteRequest(route);
        Route addedRoute = storeRouteService.addRoute(request);
        
        return Response.ok(addedRoute.toJson()).build();
    }
    
    /**
     * Delete route by id.
     *
     * @param id the id
     * @return the response
     * @throws RouteStoreServiceException the route store service exception
     */
    @DELETE
    @Path("/{id}")
    public Response deleteRouteById(@PathParam("id") String id) throws RouteStoreServiceException {
        log.info("Delete route by id: " + id);
        
        DeleteRouteRequest request = new DeleteRouteRequest(id);
        storeRouteService.deleteRoute(request);
        
        return Response.ok().build();
    }
    
    /**
     * Delete all routes.
     *
     * @return the response
     * @throws RouteStoreServiceException the route store service exception
     */
    @DELETE
    public Response deleteAllRoutes() throws RouteStoreServiceException {
        log.info("Delete all routes");
        
        storeRouteService.deleteAllRoutes();
        return Response.ok().build();
    }
    
}

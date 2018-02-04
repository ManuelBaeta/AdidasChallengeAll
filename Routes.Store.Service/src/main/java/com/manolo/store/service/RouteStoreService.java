package com.manolo.store.service;

import com.manolo.store.domain.Route;
import com.manolo.store.domain.service.AddRouteRequest;
import com.manolo.store.domain.service.DeleteRouteRequest;
import com.manolo.store.domain.service.GetRouteRequest;
import com.manolo.store.domain.service.GetRouteResponse;

/**
 * RouteStoreService defines the operations allowed by RouteStore component.
 *
 */
public interface RouteStoreService {

    /**
     * Get route by Id.
     *
     * @param request the request
     * @return the route by id
     * @throws RouteStoreServiceException the route store service exception. Causes DATABASE_ERROR or ROUTE_NOT_FOUND
     */
    public GetRouteResponse getRouteById(GetRouteRequest request) throws RouteStoreServiceException ;
    
    /**
     * Get all routes.
     *
     * @return the all routes
     * @throws RouteStoreServiceException the route store service exception. Causes DATABASE_ERROR or ROUTE_NOT_FOUND
     */
    public GetRouteResponse getAllRoutes() throws RouteStoreServiceException;
    
    
    /**
     * Add a new route.
     *
     * @param request the request
     * @return the route
     * @throws RouteStoreServiceException the route store service exception. Causes: ROUTE_ALREADY_PRESENT, DATABASE_ERROR
     */
    public Route addRoute(AddRouteRequest request) throws RouteStoreServiceException;
    
    /**
     * Delete a route by Id.
     *
     * @param request the request
     * @throws RouteStoreServiceException the route store service exception.  Causes DATABASE_ERROR or ROUTE_NOT_FOUND
     */
    public void deleteRoute(DeleteRouteRequest request) throws RouteStoreServiceException;
    
    /**
     * Delete all routes.
     *
     * @throws RouteStoreServiceException the route store service exception.  Causes DATABASE_ERROR
     */
    public void deleteAllRoutes() throws RouteStoreServiceException;
    
}

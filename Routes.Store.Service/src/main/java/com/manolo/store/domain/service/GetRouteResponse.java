package com.manolo.store.domain.service;

import java.util.ArrayList;
import java.util.Collection;

import com.manolo.store.domain.Route;

/**
 * Response container to  wraps a collection of routes retrives from storage.
 */
public class GetRouteResponse {

    /** The routes. */
    protected final Collection<Route> routes = new ArrayList<>();
    
    /**
     * Instantiates a new gets the route response.
     *
     * @param routes the routes
     */
    public GetRouteResponse(Collection<Route> routes){
        this.routes.addAll(routes);
    }
    
    /**
     * Instantiates a new gets the route response.
     *
     * @param route the route
     */
    public GetRouteResponse(Route route){
        this.routes.add(route);
    }

    /**
     * Gets the routes.
     *
     * @return the routes
     */
    public Collection<Route> getRoutes() {
        return routes;
    }

    
    /**
     * @see java.lang.Object#toString()
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GetRouteResponse [routes=").append(routes).append("]");
        return builder.toString();
    }
    
}

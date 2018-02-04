package com.manolo.store.domain.service;

import com.manolo.store.domain.Route;


/**
 * Request container to  wraps a provided Route during route add.
 *
 */
public class AddRouteRequest {

    /** The route. */
    protected Route route;
    
    /**
     * Instantiates a new adds the route request.
     *
     * @param route the route
     */
    public AddRouteRequest(Route route) {
        this.route = route;
    }

    /**
     * Gets the route.
     *
     * @return the route
     */
    public Route getRoute() {
        return route;
    }

    /**
     * @see java.lang.Object#toString()
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AddRouteRequest [route=").append(route).append("]");
        return builder.toString();
    }
    
}

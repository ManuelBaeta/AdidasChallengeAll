package com.manolo.store.domain.service;

/**
 * Request container to  wraps a routeId while getting routes.
 *
 */
public class GetRouteRequest {

    /** The route id. */
    protected String routeId;
    
    /**
     * Instantiates a new gets the route request.
     *
     * @param id the id
     */
    public GetRouteRequest(String id){
        this.routeId = id;
    }

    /**
     * Gets the route id.
     *
     * @return the route id
     */
    public String getRouteId() {
        return routeId;
    }

    /**
     * @see java.lang.Object#toString()
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GetRouteRequest [routeId=").append(routeId).append("]");
        return builder.toString();
    }

}

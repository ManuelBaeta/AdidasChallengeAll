package com.manolo.store.domain.service;

/**
 * Request container to  wraps a routeId during route delete.
 *
 */
public class DeleteRouteRequest {

    /** The route id. */
    protected String routeId;
    
    /**
     * Instantiates a new delete route request.
     *
     * @param id the id
     */
    public DeleteRouteRequest(String id) {
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
        builder.append("DeleteRouteRequest [routeId=").append(routeId).append("]");
        return builder.toString();
    }
    
}

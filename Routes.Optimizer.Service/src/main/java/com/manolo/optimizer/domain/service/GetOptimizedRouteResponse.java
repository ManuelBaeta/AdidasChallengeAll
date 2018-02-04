package com.manolo.optimizer.domain.service;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manolo.optimizer.domain.BaseRestMsg;

/**
 * GetOptimizedRouteResponse store information used while responding with optimized itineraries, such optimizedItinerariesByTime
 *  and optimizedItinerariesByHops
 *  It is Json-izable
 *
 */
public class GetOptimizedRouteResponse extends BaseRestMsg {

    /** The optimized itineraries by time. */
    @JsonProperty("itineraries_optimized_time")
    Collection<OptimizedItinerary> optimizedItinerariesByTime = new ArrayList<>();
    
    /** The optimized itineraries by hops. */
    @JsonProperty("itineraries_optimized_hops")
    Collection<OptimizedItinerary> optimizedItinerariesByHops = new ArrayList<>();

    /**
     * Instantiates a new gets the optimized route response.
     */
    public GetOptimizedRouteResponse() {
    }
    
    /**
     * Instantiates a new gets the optimized route response.
     *
     * @param optItinerariesByTime the opt itineraries by time
     * @param optItinerariesByHops the opt itineraries by hops
     */
    public GetOptimizedRouteResponse(Collection<OptimizedItinerary> optItinerariesByTime, Collection<OptimizedItinerary> optItinerariesByHops) {
        this.optimizedItinerariesByTime = optItinerariesByTime;
        this.optimizedItinerariesByHops = optItinerariesByHops;
    }

    /**
     * Gets the optimized itineraries by time.
     *
     * @return the optimized itineraries by time
     */
    public Collection<OptimizedItinerary> getOptimizedItinerariesByTime() {
        return optimizedItinerariesByTime;
    }

    /**
     * Gets the optimized itineraries by hops.
     *
     * @return the optimized itineraries by hops
     */
    public Collection<OptimizedItinerary> getOptimizedItinerariesByHops() {
        return optimizedItinerariesByHops;
    }

    /**
     * @see java.lang.Object#toString()
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GetOptimizedRouteResponse [optimizedItinerariesByTime=").append(optimizedItinerariesByTime).append(", optimizedItinerariesByHops=")
                .append(optimizedItinerariesByHops).append("]");
        return builder.toString();
    }
}

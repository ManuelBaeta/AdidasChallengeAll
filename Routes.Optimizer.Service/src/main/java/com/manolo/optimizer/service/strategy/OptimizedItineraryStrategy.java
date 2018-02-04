package com.manolo.optimizer.service.strategy;

import java.util.Collection;

import com.manolo.optimizer.domain.service.OptimizedItinerary;
import com.manolo.optimizer.domain.service.RouteNode;

/**
 * The Interface OptimizedItineraryStrategy defines the functionality provided by all the strategies
 *
 */
public interface OptimizedItineraryStrategy {

    /**
     * Gets the best itinerary by time.
     *
     * @param origin the origin
     * @param routes the routes
     * @return the best itinerary by time
     */
    public Collection<OptimizedItinerary> getBestItineraryByTime(String origin, Collection<RouteNode> routes) throws OptimizedItineraryStrategyException;
    
    /**
     * Gets the best itinerary by hops.
     *
     * @param origin the origin
     * @param routes the routes
     * @return the best itinerary by hops
     */
    public Collection<OptimizedItinerary> getBestItineraryByHops(String origin, Collection<RouteNode> routes) throws OptimizedItineraryStrategyException;
}

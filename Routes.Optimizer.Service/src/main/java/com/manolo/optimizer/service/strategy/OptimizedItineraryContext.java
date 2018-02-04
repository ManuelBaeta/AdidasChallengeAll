package com.manolo.optimizer.service.strategy;

import java.util.Collection;

import com.manolo.optimizer.domain.service.OptimizedItinerary;
import com.manolo.optimizer.domain.service.RouteNode;

/**
 * The Class OptimizedItineraryContext acts as the context on a Strategy pattern:
 *  It is instantiated with a specific strategy and delegates on it to resolve the problem.

 */
public class OptimizedItineraryContext {

    private OptimizedItineraryStrategy optimizerStrategy;

    /**
     * Instantiates a new optimized itinerary context.
     *
     * @param optimizer the optimizer
     */
    public OptimizedItineraryContext(OptimizedItineraryStrategy optimizer) {
        this.optimizerStrategy = optimizer;
    }
    
    /**
     * Gets the best itinerary by time.
     *
     * @param origin the origin
     * @param routes the routes
     * @return the best itinerary by time
     * @throws OptimizedItineraryStrategyException 
     */
    public Collection<OptimizedItinerary> getBestItineraryByTime(String origin, Collection<RouteNode> routes) throws OptimizedItineraryStrategyException {
        return optimizerStrategy.getBestItineraryByTime(origin,  routes);
    }
    
    /**
     * Gets the best itinerary by hops.
     *
     * @param origin the origin
     * @param routes the routes
     * @return the best itinerary by hops
     * @throws OptimizedItineraryStrategyException 
     */
    public Collection<OptimizedItinerary> getBestItineraryByHops(String origin, Collection<RouteNode> routes) throws OptimizedItineraryStrategyException {
        return optimizerStrategy.getBestItineraryByHops(origin, routes);
    }
  
}

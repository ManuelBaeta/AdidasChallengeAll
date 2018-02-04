package com.manolo.optimizer.service.strategy;

/**
 * Exception class for optimized itinerary strategies
 */
public class OptimizedItineraryStrategyException extends Exception{

	private static final long serialVersionUID = -7408793700414596602L;

	public OptimizedItineraryStrategyException() {
	}
	
    public OptimizedItineraryStrategyException(String errMsg) {
        super(errMsg);
    }
    
}

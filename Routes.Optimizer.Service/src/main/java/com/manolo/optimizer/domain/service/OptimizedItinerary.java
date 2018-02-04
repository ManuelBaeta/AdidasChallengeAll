package com.manolo.optimizer.domain.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class OptimizedItinerary stores an optimized itinerary
 *
 * It is Json-izable
 */
public class OptimizedItinerary {
    
    @JsonProperty("optimal_itineraries")
    private List<List<String>>itineraryList = new ArrayList<>();
    
    @JsonProperty("cost")
    private Double cost;
    
    @JsonProperty("origin")
    private String origin;
    
    @JsonProperty("destination")
    private String destination;
    
    /**
     * Instantiates a new optimized itinerary.
     */
    public OptimizedItinerary() {
    }
    
    /**
     * Instantiates a new optimized itinerary.
     *
     * @param builder the builder
     */
    public OptimizedItinerary(Builder builder) {
        this.itineraryList = builder.itineraryList;
        this.cost = builder.cost;
        this.origin = builder.origin;
        this.destination = builder.destination;
    }
    
    /**
     * The Class Builder.
     */
    public static class Builder {
        
        /** The itinerary list. */
        List<List<String>>itineraryList = new ArrayList<>();
        
        /** The cost. */
        Double cost;
        
        /** The origin. */
        String origin;   
        
        /** The destination. */
        String destination;
        
        /**
         * Instantiates a new builder.
         *
         * @param itineraryList the itinerary list
         */
        public Builder(List<List<String>>itineraryList) {
            this.itineraryList.addAll(itineraryList);
        }
        
        /**
         * Sets the cost.
         *
         * @param cost the cost
         * @return the builder
         */
        public Builder setCost(Double cost) {
            this.cost = cost;
            return this;
        }
        
        /**
         * From.
         *
         * @param origin the origin
         * @return the builder
         */
        public Builder from(String origin) {
            this.origin = origin;
            return this;
        }
        
        /**
         * To.
         *
         * @param destination the destination
         * @return the builder
         */
        public Builder to(String destination) {
            this.destination = destination;
            return this;
        }
        
        /**
         * Builds the.
         *
         * @return the optimized itinerary
         */
        public OptimizedItinerary build() {
            return new OptimizedItinerary(this);
        }
        
    }

    /**
     * Gets the itinerary list.
     *
     * @return the itinerary list
     */
    public List<List<String>> getItineraryList() {
        return itineraryList;
    }

    /**
     * Gets the cost.
     *
     * @return the cost
     */
    public Double getCost() {
        return cost;
    }

    /**
     * @see java.lang.Object#toString()
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OptimizedItinerary [itineraryList=").append(itineraryList).append(", cost=").append(cost).append(", origin=").append(origin)
                .append(", destination=").append(destination).append("]");
        return builder.toString();
    }

    /**
     * Gets the destination.
     *
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Gets the origin.
     *
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }
    
}

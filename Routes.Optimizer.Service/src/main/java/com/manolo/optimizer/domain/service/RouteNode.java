package com.manolo.optimizer.domain.service;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import com.manolo.optimizer.domain.Route;

/**
 * The Class RouteNode stores the information about a node, in a way it can be used to compose 
 * a directed graph: origen, destination and cost.
 *
 */
public class RouteNode {

    private String city;
    
    private String destination;
    
    private long cost;
    
    /**
     * Instantiates a new route node.
     *
     * @param route the route
     */
    public RouteNode(Route route) {
        this.city = route.getCity();
        this.destination = route.getDestination();
        
        DateTime dateDeparture = new DateTime(route.getDeparture());
        DateTime dateArrival = new DateTime(route.getArrival());
        
        this.cost = Minutes.minutesBetween(dateDeparture, dateArrival).getMinutes();
        //Period p = new Period(route.getDeparture().getTime(), route.getArrival().getTime());
        //this.cost =  p.getMinutes();
    }

    /**
     * For testing purposes
     * @param city
     * @param destination
     * @param cost
     */
    public RouteNode(String city, String destination, long cost) {
    	this.city = city;
    	this.destination = destination;
    	this.cost = cost;
    }
    
    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getCity() {
        return city;
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
     * Gets the cost.
     *
     * @return the cost
     */
    public long getCost() {
        return cost;
    }

    /**
     * Sets the city.
     *
     * @param city the new city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Sets the destination.
     *
     * @param destination the new destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Sets the cost.
     *
     * @param cost the new cost
     */
    public void setCost(long cost) {
        this.cost = cost;
    } 
    
    
}

package com.manolo.store.repository;

import org.springframework.data.repository.CrudRepository;

import com.manolo.store.domain.Route;

/**
 * RouteRepository is integrated with spring-data-jpa.
 * It extends spring-data-jpa's CrudRepository that provides basic CRUD operations out of the box.
 */
public interface RouteRepository extends CrudRepository<Route, String>{
    
    /**
     * Find by city and destination.
     *
     * @param city the city
     * @param destination the destination
     * @return the route
     */
    Route findByCityAndDestination(String city, String destination);
}

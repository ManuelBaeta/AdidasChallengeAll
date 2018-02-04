package com.manolo.store.service.impl;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manolo.store.domain.Route;
import com.manolo.store.domain.service.AddRouteRequest;
import com.manolo.store.domain.service.DeleteRouteRequest;
import com.manolo.store.domain.service.GetRouteRequest;
import com.manolo.store.domain.service.GetRouteResponse;
import com.manolo.store.repository.RouteRepository;
import com.manolo.store.service.BusinessExceptionCause;
import com.manolo.store.service.RouteStoreService;
import com.manolo.store.service.RouteStoreServiceException;

/**
 * The Class RouteStoreServiceImpl provides a basic implementation of RouteStoreService.
 * It delegates into RouteRepository spring-data-jpa based repository all the data access.
 */
@Service
public class RouteStoreServiceImpl implements RouteStoreService {

    private final Logger log = LoggerFactory.getLogger(RouteStoreServiceImpl.class);

    @Autowired
    private RouteRepository repository;
    
    /**
     * Gets the route by id.
     *
     * @param request the request
     * @return the route by id
     * @throws RouteStoreServiceException the route store service exception
     * @see com.manolo.store.service.RouteStoreService#getRouteById(com.manolo.store.domain.service.GetRouteRequest)
     */
    @Override
    public GetRouteResponse getRouteById(GetRouteRequest request) throws RouteStoreServiceException {
        Route route;
        try {
            route = repository.findOne(request.getRouteId()); // the entity with the given id or if none found (DataAccessException if going south)

        } catch (DataAccessException e) {
            String errMsg = String.format("Error accessing Repository (findOne). Msg: %s", e);
            log.error(errMsg);
            throw new RouteStoreServiceException(errMsg, BusinessExceptionCause.DATABASE_ERROR);
        }
        
        if (route == null) {
            log.info("There is no route with id:" + request.getRouteId());
            throw new RouteStoreServiceException("There is no route with id:" + request.getRouteId(), BusinessExceptionCause.ROUTE_NOT_FOUND);
            
        } else {
            log.info("Found Route: " + route);
        }
        return new GetRouteResponse(route);
    }

    /**
     * Gets the all routes.
     *
     * @return the all routes
     * @throws RouteStoreServiceException the route store service exception
     * @see com.manolo.store.service.RouteStoreService#getAllRoutes()
     */
    @Override
    public GetRouteResponse getAllRoutes() throws RouteStoreServiceException {
        Collection<Route> routes;
        try {
            routes = StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList()); // (DataAccessException if going south)
            
        } catch (DataAccessException e) {
            String errMsg = String.format("Error accessing Repository (findAll). Msg: %s", e);
            log.error(errMsg);
            throw new RouteStoreServiceException(errMsg, BusinessExceptionCause.DATABASE_ERROR);
        }
        
        if (routes == null || routes.isEmpty()) {
            log.info("There are no routes available");
            throw new RouteStoreServiceException("There are no routes available", BusinessExceptionCause.ROUTE_NOT_FOUND);
            
        } else {
            log.info("Found routes: " + routes);
        }
            
        return new GetRouteResponse(routes);
    }

    /**
     * Adds the route.
     *
     * @param request the request
     * @return the route
     * @throws RouteStoreServiceException the route store service exception
     * @see com.manolo.store.service.RouteStoreService#addRoute(com.manolo.store.domain.service.AddRouteRequest)
     */
    @Override
    public Route addRoute(AddRouteRequest request) throws RouteStoreServiceException {
        Route addedRoute;
        String errMsg;
        try {
            if (repository.findByCityAndDestination(request.getRoute().getCity(), request.getRoute().getDestination()) != null) {
                errMsg = String.format("Unable to add route, there is already a route with city: %s and destination: %s"
                        , request.getRoute().getCity(), request.getRoute().getDestination());
                log.error(errMsg);
                throw new RouteStoreServiceException(errMsg, BusinessExceptionCause.ROUTE_ALREADY_PRESENT);        
            }
            
            addedRoute = repository.save(request.getRoute());      // (DataAccessException if going south)
            log.info("Added route with id: " + addedRoute.getId());
            
        } catch (DataAccessException e) {
            errMsg = String.format("Error accessing Repository (save). Msg: %s", e);
            log.error(errMsg);
            throw new RouteStoreServiceException(errMsg, BusinessExceptionCause.DATABASE_ERROR);
        }
        
        return  addedRoute;
    }

    /**
     * Delete route.
     *
     * @param request the request
     * @throws RouteStoreServiceException the route store service exception
     * @see com.manolo.store.service.RouteStoreService#deleteRoute(com.manolo.store.domain.service.DeleteRouteRequest)
     */
    @Override
    @Transactional
    public void deleteRoute(DeleteRouteRequest request) throws RouteStoreServiceException {
        String errMsg = null;
        try {
            if (repository.exists(request.getRouteId())) {
                repository.delete(request.getRouteId());        // (DataAccessException if going south)
                log.info("Deleted route with id: " + request.getRouteId());
                
            } else {
                errMsg = String.format("There is no route with provided id: %s", request.getRouteId());
                log.info(errMsg);
                throw new RouteStoreServiceException(errMsg, BusinessExceptionCause.ROUTE_NOT_FOUND);
            }
        } catch (DataAccessException ex) {
            errMsg = String.format("Error accessing Repository (exists/delete). Msg: %s", ex);
            log.error(errMsg);
            throw new RouteStoreServiceException(errMsg, BusinessExceptionCause.DATABASE_ERROR);
        }
    }

    /**
     * Delete all routes.
     *
     * @throws RouteStoreServiceException the route store service exception
     * @see com.manolo.store.service.RouteStoreService#deleteAllRoutes()
     */
    @Override
    public void deleteAllRoutes() throws RouteStoreServiceException {
        try {
            repository.deleteAll(); // (DataAccessException if going south)
            log.info("Deleted all routes");
            
        } catch (DataAccessException ex) {
            String errMsg = String.format("Error accessing Repository (deleteAll). Msg: %s", ex);
            log.error(errMsg);
            throw new RouteStoreServiceException(errMsg, BusinessExceptionCause.DATABASE_ERROR);
        }
    }
    
    
}

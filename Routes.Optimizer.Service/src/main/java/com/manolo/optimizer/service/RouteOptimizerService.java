package com.manolo.optimizer.service;

import com.manolo.optimizer.domain.service.GetOptimizedRouteRequest;
import com.manolo.optimizer.domain.service.GetOptimizedRouteResponse;

/**
 * The Interface RouteOptimizerService defined the service  provide by RouteOptimizer component.
 */
public interface RouteOptimizerService {

   /**
    * Gets the optimized routes.
    *
    * @param request the request
    * @return the optimized routes
    * @throws RouteOptimizerServiceException the route optimizer service exception
    */
   GetOptimizedRouteResponse getOptimizedRoutes(GetOptimizedRouteRequest request) throws RouteOptimizerServiceException;
}

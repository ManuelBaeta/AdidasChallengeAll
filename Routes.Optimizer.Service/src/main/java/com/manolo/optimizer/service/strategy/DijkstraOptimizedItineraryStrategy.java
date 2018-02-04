package com.manolo.optimizer.service.strategy;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manolo.optimizer.domain.service.OptimizedItinerary;
import com.manolo.optimizer.domain.service.RouteNode;
import com.manolo.optimizer.service.impl.RouteOptimizerServiceImpl;

import es.usc.citius.hipster.algorithm.Algorithm.SearchResult;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.SearchProblem;

/**
 * The Class DijkstraOptimizedItineraryStrategy resolve the problem using Dijkstra algorithm
 *
 */
public class DijkstraOptimizedItineraryStrategy implements OptimizedItineraryStrategy {

    private final Logger log = LoggerFactory.getLogger(RouteOptimizerServiceImpl.class);
    
    /**
     * Gets the best itinerary by time.
     *
     * @param origin the origin
     * @param routes the routes
     * @return the best itinerary by time
     * @see com.manolo.optimizer.service.strategy.OptimizedItineraryStrategy#getBestItineraryByTime(java.lang.String, java.util.Collection)
     */
    @Override
    public Collection<OptimizedItinerary> getBestItineraryByTime(String origin, Collection<RouteNode> routes) throws OptimizedItineraryStrategyException {
        log.info(String.format("** Getting best itineraries (based on time) from %s, using Dijkstra algorithm. **", origin));
        
        if (!isOriginPresent(routes, origin))
        	throw new OptimizedItineraryStrategyException(String.format("Origin city: %s is not present.", origin));
        	
        Set<String> allDestinations = getDestinations(routes, origin);
        HipsterDirectedGraph<String,Long> graph =  doGraphWithTimes(routes);
        
        // call Dijkstra for the same origen and all the destinations ...
        Collection<OptimizedItinerary> results = allDestinations.stream()
                .map(destination -> doDijkstra(graph, origin, destination))
                .collect(Collectors.toList());
        
        return results;
    }

    /**
     * Gets the best itinerary by hops.
     *
     * @param origin the origin
     * @param routes the routes
     * @return the best itinerary by hops
     * @see com.manolo.optimizer.service.strategy.OptimizedItineraryStrategy#getBestItineraryByHops(java.lang.String, java.util.Collection)
     */
    @Override
    public Collection<OptimizedItinerary> getBestItineraryByHops(String origin, Collection<RouteNode> routes) throws OptimizedItineraryStrategyException{
        log.info(String.format("** Getting best itineraries (based on hops) from %s, using Dijkstra algorithm. **", origin));
        
        if (!isOriginPresent(routes, origin))
        	throw new OptimizedItineraryStrategyException(String.format("Origin city: %s is not present.", origin));
        
        Set<String> allDestinations = getDestinations(routes, origin);
        HipsterDirectedGraph<String,Long> graph =  doGraphHops(routes);
        
        Collection<OptimizedItinerary> results = allDestinations.stream()
                .map(destination -> doDijkstra(graph, origin, destination))
                .collect(Collectors.toList());
        
        return results;
    }

    /**
     * Do dijkstra to search fast path from prigen to destination
     * @param graph
     * @param origin
     * @param destination
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private OptimizedItinerary doDijkstra(HipsterDirectedGraph<String,Long> graph, String origin, String destination) {
        log.info(String.format("----- Doing Dijkstra for origen: %s, and destination: %s -----", origin, destination));
        SearchResult res = null;
        SearchProblem p = GraphSearchProblem
                .startingFrom(origin)
                .in(graph)
                .takeCostsFromEdges()
                .build();

        res=  Hipster.createDijkstra(p).search(destination);
        WeightedNode node = (WeightedNode)res.getGoalNode();
        log.info(String.format("Best itineraries: %s, cost final: %s", res.getOptimalPaths(), node.getCost()));
        
        return new OptimizedItinerary.Builder(res.getOptimalPaths())
                .setCost( Double.parseDouble(node.getCost().toString()))
                .from(origin)
                .to(destination)
                .build();
    }
    
    /**
     * Create a directed graph weighted based on times.
     * @param routes
     * @return
     */
    private HipsterDirectedGraph<String,Long> doGraphWithTimes(Collection<RouteNode> routes) {
        log.info("Doing directed graph for defined routes. Cost based on time.");
        GraphBuilder<String,Long> graphBuilder =  GraphBuilder.<String,Long>create();
        routes.forEach(route -> {
            graphBuilder.connect(route.getCity()).to(route.getDestination()).withEdge(route.getCost()); 
        });
        return graphBuilder.createDirectedGraph();
    }
    
    /**
     * Create a directed graph weighted the same on all the edges.
     * @param routes
     * @return
     */
    private HipsterDirectedGraph<String,Long> doGraphHops(Collection<RouteNode> routes) {
        log.info("Doing directed graph for defined routes. Cost is equal to one(to get hops).");
        GraphBuilder<String,Long> graphBuilder =  GraphBuilder.<String,Long>create();
        routes.forEach(route -> {
            graphBuilder.connect(route.getCity()).to(route.getDestination()).withEdge(1L); 
        });
        return graphBuilder.createDirectedGraph();
    }
    
    /**
     * Get all the valid destinations, filtering origin itself.
     * @param routes
     * @param origen
     * @return
     */
    private Set<String> getDestinations(Collection<RouteNode> routes, String origen) {
        Set<String> allDestinations = routes.stream()
                    .map(route ->  route.getDestination())
                    .filter(destination -> ! destination.equals(origen))
                    .collect(Collectors.toSet());
        
        log.info(String.format("Got destinations: %s", allDestinations));
        return allDestinations;
    }
 
    private boolean isOriginPresent(Collection<RouteNode> routes, String origen) {
    	Set<RouteNode> routesContainingOrigin = routes.stream()
    		.filter(route -> route.getDestination().equals(origen))
    		.collect(Collectors.toSet());
    	
    	if (routesContainingOrigin == null || routesContainingOrigin.isEmpty()) 
    		return false;
    	else
    		return true;
    	
    }
    
}

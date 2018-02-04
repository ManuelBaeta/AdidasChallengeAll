package com.manolo.optimizer.domain;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manolo.optimizer.service.JsonException;

/**
 * It represent a route: city, destination, departure, arrival ...
 *
 */
public class Route extends BaseRestMsg{
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("city")
    private String city;
    
    @JsonProperty("destination")
    private String destination;
    
    @JsonProperty("departure")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy hh:mm:ss")
    private Date departure;
    
    @JsonProperty("arrival")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy hh:mm:ss")
    private Date arrival;
    
    /**
     * Instantiates a new route.
     */
    public Route() {
    }

    /**
     * Only for testing purposes
     */
    public Route(String city, String destination, Date departure, Date arrival) {
    	this.city = city;
    	this.destination = destination;
    	this.departure = departure;
    	this.arrival = arrival;
    }
    
    /**
     * Only for testing purposes
     */

    /**
     * From json.
     *
     * @param json the json
     * @return the route
     * @throws JsonException the json exception
     */
    public static Route fromJson(String json) throws JsonException {
        ObjectMapper mapper = new ObjectMapper();
        
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {            
            return mapper.readValue(json, Route.class);
        } catch (IOException e) {
            throw new JsonException("Error unmarshalling json: " + e);
        }
    }
    
    /**
     * From json collection.
     *
     * @param json the json
     * @return the list
     * @throws JsonException the json exception
     */
    @SuppressWarnings("unchecked")
    public static List<Route> fromJsonCollection(String json) throws JsonException {
        ObjectMapper mapper = new ObjectMapper();
        
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {            
            return mapper.readValue(json, new TypeReference<List<Route>>() {});
        } catch (IOException e) {
            throw new JsonException("Error unmarshalling json: " + e);
        }
    }
    
    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
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
     * Gets the departure.
     *
     * @return the departure
     */
    public Date getDeparture() {
        return departure;
    }

    /**
     * Gets the arrival.
     *
     * @return the arrival
     */
    public Date getArrival() {
        return arrival;
    }


    /**
     * @see java.lang.Object#toString()
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Route [id=").append(id).append(", city=").append(city).append(", destination=").append(destination).append(", departure=")
                .append(departure).append(", arrival=").append(arrival).append("]");
        return builder.toString();
    }
    
}

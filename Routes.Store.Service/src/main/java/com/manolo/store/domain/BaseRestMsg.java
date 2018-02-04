package com.manolo.store.domain;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.manolo.store.service.JsonException;

/**
 * The Class BaseRestMsg is the top of the hierarchy for all the classed that could be serialized/deserialized
 * provides some methods.
 *
 * @author gzml7g
 */
public class BaseRestMsg {
    
    /**
     * To json.
     *
     * @return the string
     * @throws JsonException the json exception
     */
    public String toJson() throws JsonException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT,true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        
        StringWriter sw = new StringWriter();
        try {
            mapper.writeValue(sw,this);
            return sw.toString();
        } catch (IOException e) {
            throw new JsonException("Error marshalling " +  this, e);
        }
    }

    /**
     * To json collection.
     *
     * @param <T> the generic type
     * @param collection the collection
     * @return the string
     * @throws JsonException the json exception
     */
    public static <T> String toJsonCollection(Collection<T> collection) throws JsonException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT,true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        
        StringWriter sw = new StringWriter();
        try {
            mapper.writerFor(new TypeReference<Collection<T>>(){}).writeValue(sw, collection);
            return sw.toString();
        } catch (IOException e) {
            throw new JsonException("Error marshalling " +  collection, e);
        }
    }
    
    /**
     * From json.
     *
     * @param <T> the generic type
     * @param json the json
     * @return the t
     * @throws JsonException the json exception
     */
    public static <T> T fromJson(String json) throws JsonException {
        ObjectMapper mapper = new ObjectMapper();
        
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {            
            return mapper.readValue(json, new TypeReference<T>() {});
        } catch (IOException e) {
            throw new JsonException("Error unmarshalling json: " + e);
        }
    }
}

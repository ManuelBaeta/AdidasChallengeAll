package com.manolo.store;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.log4j.MDC;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.manolo.store.rest.RouteResource;
import com.manolo.store.service.BusinessExceptionCause;
import com.manolo.store.service.JsonException;
import com.manolo.store.service.RouteStoreServiceException;

/**
 * Extend Jersey JAX-RS ResourceConfig class to define:
 *   Resource classes
 *   Use of Jackson Json provider
 *   Exception mappers
 *   Filters.
 */
@Component
@ApplicationPath(value="/routes-store-service/api")
public class JerseyConfiguration extends ResourceConfig{

    private final static Logger log = LoggerFactory.getLogger(JerseyConfiguration.class);
    
    /**
     * Instantiates a new jersey configuration.
     */
    public JerseyConfiguration() {
        this.register(JacksonFeature.class);
        
        this.register(RouteResource.class);
        
        this.register(JsonExceptionMapper.class);
        
        this.register(RouteStoreServiceExceptionMapper.class);
        
        this.register(RequestIdFilter.class);
                
        this.property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, "true");
    }
        
    
    /**
     * Request filter to inject RequestId on context, to be used by slf4j.
     */
    public static class RequestIdFilter implements ContainerRequestFilter {

        /** The Constant RequestId. */
        public static final String RequestId = "RequestId";
        
        private AtomicLong counter = new AtomicLong(1);
        
        /**
         * Filter.
         *
         * @param requestContext the request context
         * @throws IOException Signals that an I/O exception has occurred.
         * @see javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container.ContainerRequestContext)
         */
        @Override
        public void filter(ContainerRequestContext requestContext) throws IOException {
            MDC.put(RequestId, counter.getAndIncrement()); 
        }
        
    }
    
    /**
     * JsonExceptionMapper translates JsonException errors into meaningful HTTP Status Code 400 (Bad Request)
     */
    public static class JsonExceptionMapper implements ExceptionMapper<JsonException> {

        /**
         * To response.
         *
         * @param exception the exception
         * @return the response
         * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
         */
        @Override
        public Response toResponse(JsonException exception) {
            log.info("Translated exception: " +  exception +  " into HTTP Status Code 400.");
            return Response.status(Status.BAD_REQUEST).build();
        }
        
    }
    
    /**
     * RouteStoreServiceExceptionMapper translates RouteStoreServiceException into HTTP Status Codes based 
     * on the error business cause:
     *  ROUTE_NOT_FOUND: INTO 404
     *  DATA_BASE_ERROR: INTO 500
     *  ROUTE_ALREADY_PRESENT: INTO 409 (Conflict)
     */
    public static class RouteStoreServiceExceptionMapper implements ExceptionMapper<RouteStoreServiceException> {

        /**
         * To response.
         *
         * @param exception the exception
         * @return the response
         * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
         */
        @Override
        public Response toResponse(RouteStoreServiceException exception) {
            if (exception.getBusinessCause() == BusinessExceptionCause.ROUTE_NOT_FOUND) {
                log.info("Translated exception: " + exception +  " into HTTP Status Code 404.");
                return Response.status(Status.NOT_FOUND).build();
                
            } else if (exception.getBusinessCause() == BusinessExceptionCause.DATABASE_ERROR) {
                log.error("DataBase Exception: " + exception + " translated into HTTP Status Code 500.");
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
                
            } else if (exception.getBusinessCause() == BusinessExceptionCause.ROUTE_ALREADY_PRESENT) {
                log.error("Translated exception: " + exception + " translated into HTTP Status Code 409.");
                return Response.status(Status.CONFLICT).build();
            }
            
            log.error("Unexpected exception: " + exception + " translated into HTTP Status Code 500.");
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }      
    }
}

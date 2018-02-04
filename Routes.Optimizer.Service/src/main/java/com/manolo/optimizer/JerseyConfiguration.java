package com.manolo.optimizer;

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

import com.manolo.optimizer.rest.OptimizedRouteResource;
import com.manolo.optimizer.service.BusinessExceptionCause;
import com.manolo.optimizer.service.JsonException;
import com.manolo.optimizer.service.RouteOptimizerServiceException;

/**
 * Extend Jersey JAX-RS ResourceConfig class to define:
 *   Resource classes
 *   Use of Jackson Json provider
 *   Exception mappers
 *   Filters.
 */
@Component
@ApplicationPath(value="/routes-optimizer-service/api")
public class JerseyConfiguration extends ResourceConfig {

    private final static Logger log = LoggerFactory.getLogger(JerseyConfiguration.class);
    
    /**
     * Instantiates a new jersey configuration.
     */
    public JerseyConfiguration() {
        this.register(JacksonFeature.class);
        
        this.register(OptimizedRouteResource.class);
        
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
     *  ORIGIN_CITY_NOT_FOUND: INTO 404
     *  ROUTE_STORE_SERVICE_BAD_RESPONSE: INTO 500
     */
    public static class RouteStoreServiceExceptionMapper implements ExceptionMapper<RouteOptimizerServiceException> {

        /**
         * To response.
         *
         * @param exception the exception
         * @return the response
         * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
         */
        @Override
        public Response toResponse(RouteOptimizerServiceException exception) {
            if (exception.getBusinessCause() == BusinessExceptionCause.ORIGIN_CITY_NOT_FOUND) {
                log.info("Translated exception: " + exception +  " into HTTP Status Code 404.");
                return Response.status(Status.NOT_FOUND).build();
                
            } else if (exception.getBusinessCause() == BusinessExceptionCause.ROUTE_STORE_SERVICE_BAD_RESPONSE) {
                log.info("Translated exception: " + exception +  " into HTTP Status Code 500.");
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
                
            } else if (exception.getBusinessCause() == BusinessExceptionCause.ROUTE_STORE_SERVICE_UNAVAILABLE) {
                log.info("Translated exception: " + exception +  " into HTTP Status Code 503.");
                return Response.status(Status.SERVICE_UNAVAILABLE).build();
            	
            } else if (exception.getBusinessCause() == BusinessExceptionCause.ROUTE_STORE_SERVICE_NO_ROUTES) {
                log.info("Translated exception: " + exception +  " into HTTP Status Code 404.");
                return Response.status(Status.NOT_FOUND).build();
                
            } else if (exception.getBusinessCause() == BusinessExceptionCause.ROUTE_STORE_SERVICE_RESPONSE_WITH_ERRORS) { 
                log.info("Translated exception: " + exception +  " into HTTP Status Code 500.");
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
            
            log.error("Unexpected exception: " + exception + " translated into HTTP Status Code 500.");
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }      
    }
}

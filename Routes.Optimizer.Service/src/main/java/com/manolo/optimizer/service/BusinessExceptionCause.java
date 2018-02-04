package com.manolo.optimizer.service;

/**
 * The Enum BusinessExceptionCause contains al business root causes of error.
 *
 */
public enum BusinessExceptionCause {
    
    /** The origin city not found. */
    ORIGIN_CITY_NOT_FOUND, 
    /** The route store service bad response. */
    ROUTE_STORE_SERVICE_BAD_RESPONSE,
    /** Remote service unavailable **/
    ROUTE_STORE_SERVICE_UNAVAILABLE,
    /** There are no routes defined **/
    ROUTE_STORE_SERVICE_NO_ROUTES,
    /** Backend response with errors **/
    ROUTE_STORE_SERVICE_RESPONSE_WITH_ERRORS
    
}

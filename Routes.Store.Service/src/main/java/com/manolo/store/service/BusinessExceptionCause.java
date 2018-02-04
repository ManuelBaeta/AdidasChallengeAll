package com.manolo.store.service;

/**
 * BusinessExceptionCause enumerates all the expected Busines-related errors...
 */
public enum BusinessExceptionCause {

    /** The route not found. */
    ROUTE_NOT_FOUND,
    
    /** database error. */
    DATABASE_ERROR,
    
    /** The route already present. */
    ROUTE_ALREADY_PRESENT
    
}

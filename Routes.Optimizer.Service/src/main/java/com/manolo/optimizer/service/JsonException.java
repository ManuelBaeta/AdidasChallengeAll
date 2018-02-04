package com.manolo.optimizer.service;

/**
 * The Class JsonException wraps all Json exception raised by Jackson library
 *
 */
public class JsonException extends Exception{
    
    private static final long serialVersionUID = 1158026984872965535L;

    /**
     * Instantiates a new json exception.
     */
    public JsonException() {
    }
    
    /**
     * Instantiates a new json exception.
     *
     * @param errMsg the err msg
     */
    public JsonException(String errMsg) {
        super(errMsg);
    }
    
    /**
     * Instantiates a new json exception.
     *
     * @param errMsg the err msg
     * @param e the e
     */
    public JsonException(String errMsg, Throwable e) {
        super(errMsg, e);
    }
}

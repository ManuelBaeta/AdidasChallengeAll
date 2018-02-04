package com.manolo.store.service;

/**
 * JsonException encapsulate all JSON errors from Jackson library.
 */
public class JsonException extends Exception {
    
    private static final long serialVersionUID = -2832225698025697732L;

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

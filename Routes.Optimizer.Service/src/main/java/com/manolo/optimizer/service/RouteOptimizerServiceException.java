package com.manolo.optimizer.service;

/**
 * The Class RouteOptimizerServiceException wraps all service layer errors.
 * It allows to define a business root cause using businessCause
 *
 */
public class RouteOptimizerServiceException extends Exception{
    
    private static final long serialVersionUID = 5066605348489407999L;
    
    /** The business cause. */
    protected BusinessExceptionCause businessCause;
    
    /**
     * Instantiates a new route optimizer service exception.
     */
    public RouteOptimizerServiceException() {
    }
    
    /**
     * Instantiates a new route optimizer service exception.
     *
     * @param errMsg the err msg
     */
    public RouteOptimizerServiceException(String errMsg) {
        super(errMsg);
    }
    
    /**
     * Instantiates a new route optimizer service exception.
     *
     * @param errMsg the err msg
     * @param cause the cause
     */
    public RouteOptimizerServiceException(String errMsg, BusinessExceptionCause cause) {
        super(errMsg);
        this.businessCause = cause;
    }
    
    /**
     * Instantiates a new route optimizer service exception.
     *
     * @param errMsg the err msg
     * @param excp the excp
     */
    public RouteOptimizerServiceException(String errMsg, Throwable excp) {
        super(errMsg, excp);
    }

    /**
     * Gets the business cause.
     *
     * @return the business cause
     */
    public BusinessExceptionCause getBusinessCause() {
        return businessCause;
    }

}

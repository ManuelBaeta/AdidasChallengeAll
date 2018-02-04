package com.manolo.store.service;

/**
 *  RouteStoreServiceException encapsulate all the exceptions throw by service layer
 *  Use businessCause to define an specific business root cause (if any).
 *
 */
public class RouteStoreServiceException extends Exception{

    private static final long serialVersionUID = 6473586112895226627L;
    
    /** The business cause. */
    protected BusinessExceptionCause businessCause;
    
    /**
     * Instantiates a new route store service exception.
     */
    public RouteStoreServiceException() {
    }
    
    /**
     * Instantiates a new route store service exception.
     *
     * @param errMsg the err msg
     */
    public RouteStoreServiceException(String errMsg) {
        super(errMsg);
    }
    
    /**
     * Instantiates a new route store service exception.
     *
     * @param errMsg the err msg
     * @param cause the cause
     */
    public RouteStoreServiceException(String errMsg, BusinessExceptionCause cause) {
        super(errMsg);
        this.businessCause = cause;
    }
    
    /**
     * Instantiates a new route store service exception.
     *
     * @param errMsg the err msg
     * @param excp the excp
     */
    public RouteStoreServiceException(String errMsg, Throwable excp) {
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

    /**
     * @see java.lang.Throwable#toString()
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RouteStoreServiceException [businessCause=").append(businessCause).append(", message=").append(getMessage()).append("]");
        return builder.toString();
    }
    
    

}

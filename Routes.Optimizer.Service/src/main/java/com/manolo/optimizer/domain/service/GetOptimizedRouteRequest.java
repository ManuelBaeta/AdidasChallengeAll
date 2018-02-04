package com.manolo.optimizer.domain.service;

/**
 * GetOptimizedRouteRequest store information used while requesting for optimized routes, such originCity
 *
 */
public class GetOptimizedRouteRequest {

    /** The origin city. */
    protected String originCity;
        
    protected String authorizationHeader;
    
    public String getAuthorizationHeader() {
		return authorizationHeader;
	}

	/**
     * Instantiates a new gets the optimized route request.
     */
    public GetOptimizedRouteRequest() {        
    }

    /**
     * Instantiates a new gets the optimized route request.
     * @param city
     */
    public GetOptimizedRouteRequest(String city) {
        this.originCity = city;
    }
    
    /**
     * Instantiates a new gets the optimized route request.
     *
     * @param city the city
     */
    public GetOptimizedRouteRequest(String city, String authorizationHeader) {
        this.originCity = city;
        this.authorizationHeader = authorizationHeader;
    }
    
    /**
     * Gets the origin city.
     *
     * @return the origin city
     */
    public String getOriginCity() {
        return originCity;
    }


    @Override
	public String toString() {
		return "GetOptimizedRouteRequest [originCity=" + originCity + ", authorizationHeader=" + authorizationHeader
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((originCity == null) ? 0 : originCity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GetOptimizedRouteRequest other = (GetOptimizedRouteRequest) obj;
		if (originCity == null) {
			if (other.originCity != null)
				return false;
		} else if (!originCity.equals(other.originCity))
			return false;
		return true;
	}
    
}

package com.manolo.securetoken.config;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="jwtsecurity")
public class SecurityParametersConfig {

	@NotNull
	@NotEmpty
	private String signingKey;
	
	private int encondingStrength = 256;
	
	private String securityRealm = "RouteSystemClient";
	
	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	@NotNull
	@NotEmpty
	private String clientId;
	
	@NotNull
	@NotEmpty
	private String clientSecret;
	
	private String grantType = "password";
	
	private String scopeRead = "read";
	
	private String scopeWrite = "write";
	
	private String resourceId = "RoutesResourceId";
	
	public SecurityParametersConfig() {
	}

	public String getSigningKey() {
		return signingKey;
	}

	public void setSigningKey(String signingKey) {
		this.signingKey = signingKey;
	}

	public int getEncondingStrength() {
		return encondingStrength;
	}

	public void setEncondingStrength(int encondingStrength) {
		this.encondingStrength = encondingStrength;
	}

	public String getSecurityRealm() {
		return securityRealm;
	}

	public void setSecurityRealm(String securityRealm) {
		this.securityRealm = securityRealm;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getScopeRead() {
		return scopeRead;
	}

	public void setScopeRead(String scopeRead) {
		this.scopeRead = scopeRead;
	}

	public String getScopeWrite() {
		return scopeWrite;
	}

	public void setScopeWrite(String scopeWrite) {
		this.scopeWrite = scopeWrite;
	}

	@Override
	public String toString() {
		return "SecurityParametersConfig [signingKey=" + signingKey + ", encondingStrength=" + encondingStrength
				+ ", securityRealm=" + securityRealm + ", clientId=" + clientId + ", clientSecret=" + clientSecret
				+ ", grantType=" + grantType + ", scopeRead=" + scopeRead + ", scopeWrite=" + scopeWrite
				+ ", resourceId=" + resourceId + "]";
	}
	
}

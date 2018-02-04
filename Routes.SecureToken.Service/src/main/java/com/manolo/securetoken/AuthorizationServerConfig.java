package com.manolo.securetoken;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.manolo.securetoken.config.SecurityParametersConfig;

/**
 * Once authenticationManager, JwtTokenStore and JwtAccessTokenConverter are defined, they are used to setup  authorizationServer configuration:
 *   ClientDetailsService is configured: It will use a in memory storage and will add hardcoded example (clientId, secret, its 
 *       allowed grant types, scopes and resources)
 *   
 *   AuthorizationEndpoint (Token) is configured: Endpoint is configured to use JwtTokenStore, JwtAccessTokenConverter and authenticationManager
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends  AuthorizationServerConfigurerAdapter{
	
	@Autowired
	private SecurityParametersConfig jwtConfiguration;
	
	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
		configurer
		        .inMemory()
		        .withClient(jwtConfiguration.getClientId())
		        .secret(jwtConfiguration.getClientSecret())
		        .authorizedGrantTypes(jwtConfiguration.getGrantType())
		        .scopes(jwtConfiguration.getScopeRead(), jwtConfiguration.getScopeWrite())
		        .resourceIds(jwtConfiguration.getResourceId());
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
		endpoints.tokenStore(tokenStore)
		        .accessTokenConverter(accessTokenConverter)
		        .tokenEnhancer(enhancerChain)
		        .authenticationManager(authenticationManager);
	}
}

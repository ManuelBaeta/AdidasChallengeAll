package com.manolo.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * It defines security polices regarding its rol as ResourceServer.
 * It uses a TokenServices (DefaultTokenServices) configured to use a Token Store (JwtTokenStore) configured with a JwtAccessTokenConverter
 * able to decrypt tokens ( it uses same symetric key used by Authorization Server)
 */
@Profile(value = {"development", "production"})
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Value("${jwtsecurity.resourceId}")
	private String resourceIds = "RoutesResourceId";		
	
	@Value("${jwtsecurity.signingKey}")
	private String signingKey = "MaYzkSjmkzPC57L";
		    
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
    	 resources.resourceId(resourceIds).tokenServices(tokenServices());
    }
 
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }
 
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signingKey);
        return converter;
    }
 
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }
    
}

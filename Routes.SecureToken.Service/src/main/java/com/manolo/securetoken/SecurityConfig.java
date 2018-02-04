package com.manolo.securetoken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.manolo.securetoken.config.SecurityParametersConfig;

/**
 * It provides base security on authz server (http config) like no session management and http basic ( to hook up AuthManager for resource
 * ownner credentials.
 * It also setup several security components:
 *   AuthenticationManager: It will delegate into UserDetailsService and will use ShaPasswordEncoder as passwd encoder
 *   TokenService: It will pick DefaultTokenServices implementation, it will delegate into TokenStore (JwtTokenStore).
 *                 JwtTokenStore uses JwtAccessTokenConverter to sign jwt token
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private SecurityParametersConfig jwtConfiguration;
	
	
	/**
	 * Authentication Manager to authenticate clients (against pre-defined id and secret)
	 */
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	/**
	 * Auth Manager requires DAO to access to resource owner information(user/passwd) and 
	 * a password encoder to avoid storing passwds as clear text. 
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService( userDetailsService() );
		        //.passwordEncoder(new ShaPasswordEncoder(jwtConfiguration.getEncondingStrength()));
	}
	
	
	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("admin.admin").password("admin").roles("ADMIN").build());
		return manager;
	}
	

	
	/**
	 * Configure auth server basic security:
	 *   Dont use sessions
	 *   Use http basic with defined real 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		        .sessionManagement()
		        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Spring Security will never create an HttpSession and it will never use it to obtain the SecurityContext
		        .and()
		        .httpBasic()
		        .realmName(jwtConfiguration.getSecurityRealm()) //use http basic with given security realm
		        .and()
		        .csrf()
		        .disable();
	}
	
	/**
	 * JwtAccessTokenConverter is used by JwtTokenStore to handle jwt token (in this case to sign tokens=
	 * @return
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(jwtConfiguration.getSigningKey());
		return converter;
	}

	/**
	 * TokenStore is uded by JwtTokenServices. For jwt is not a real store, as token are not stored, they are just signed.
	 * @return
	 */
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	/**
	 * DefaultTokenServices is by default TokenServices implementation.It delegates into JwtTokenStore.
	 * @return
	 */
	@Bean
	@Primary //Making this primary to avoid any accidental duplication with another token service instance of the same name
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}

	
}

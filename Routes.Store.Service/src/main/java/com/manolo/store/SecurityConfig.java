package com.manolo.store;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Define base setup for security. Defining own @EnableWebSecurity overrides spring boot default: http basic for all endpoints
 * It defines no session Management and  only authenticated users can reach business api endpoints.
 *
 */
@Configuration
@EnableWebSecurity
@Profile(value = {"development", "production"})
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Spring Security will never create an HttpSession and it will never use it to obtain the SecurityContext
		    .and()
                .requestMatchers()
            .and()
		        .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/routes-store-service/api/**" ).hasRole("ADMIN")
            .and()
		        .csrf()
		        .disable();
	}
}

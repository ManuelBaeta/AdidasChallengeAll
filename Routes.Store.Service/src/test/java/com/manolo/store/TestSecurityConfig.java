package com.manolo.store;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@Profile(value = {"testing"})
public class TestSecurityConfig extends WebSecurityConfigurerAdapter{
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
                .antMatchers("/routes-store-service/api/**" ).permitAll()
            .and()
		        .csrf()
		        .disable();
	}
}

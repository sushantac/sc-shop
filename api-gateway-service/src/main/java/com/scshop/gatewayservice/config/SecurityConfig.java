package com.scshop.gatewayservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;

//@EnableWebSecurity
@Configuration
public class SecurityConfig /*extends WebSecurityConfigurerAdapter*/ {

	
	@Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
	private String ISSUER_URL; 
	
	
	@Bean
	JwtDecoder jwtDecoder() {
		return JwtDecoders.fromIssuerLocation(ISSUER_URL);
	}
	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//			.authorizeRequests()
//				.anyRequest().authenticated()
//				.and()
//			//.oauth2Login()
//				//.permitAll();
//				//.and()
//			.oauth2Client();
//	}
}

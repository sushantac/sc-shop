package com.scshop.gatewayservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class TokenExchangeGlobalFilter implements GlobalFilter {

	final Logger logger = LoggerFactory.getLogger(TokenExchangeGlobalFilter.class);

	@Autowired
	WebClient.Builder webClientBuilder;

	@Value("${application.security.authorizationServer.host}")
	private String AUTHORIZATION_SERVER_HOST; 
	
	@Value("${application.security.authorizationServer.port}")
	private Integer AUTHORIZATION_SERVER_PORT; 
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		String bearerToken = exchange.getRequest().getHeaders().getFirst("Authorization");
		if (bearerToken == null || bearerToken.isEmpty()) {
			return chain.filter(exchange);
		}

		return webClientBuilder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.build().post()
				.uri(uriBuilder -> uriBuilder.scheme("http").host(AUTHORIZATION_SERVER_HOST).port(AUTHORIZATION_SERVER_PORT)
						.path("/auth/realms/sc-shop/protocol/openid-connect/token").build())

				.body(BodyInserters.fromFormData("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange")
						.with("client_id", "js-console").with("scope", "product")
						.with("subject_token", bearerToken.substring(7, bearerToken.length())))

				.retrieve().bodyToMono(AccessToken.class).flatMap(s -> {
					logger.trace("Exchanged Access Token: " + s.getAccess_token());

					String authorizationHeaderWithExchangedToken = "Bearer " + s.getAccess_token();
					exchange.getRequest().mutate().header("Authorization", authorizationHeaderWithExchangedToken);

					return chain.filter(exchange);
				});
	}

}

class AccessToken {

	private String access_token;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
}
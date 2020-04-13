package com.scshop.orders.orderservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Bean
	@Qualifier("webClient")
	public WebClient webClient(ClientRegistrationRepository clientRegistrationRepository,
			OAuth2AuthorizedClientRepository authorizedClientRepository) {

		ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
				clientRegistrationRepository, authorizedClientRepository);

		return WebClient.builder().apply(oauth2.oauth2Configuration()).build();
	}

	@Bean
	@LoadBalanced
	@Qualifier("loadBalancedWebClient")
	public WebClient loadBalancedWebClient(ClientRegistrationRepository clientRegistrationRepository,
			OAuth2AuthorizedClientRepository authorizedClientRepository) {

		ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
				clientRegistrationRepository, authorizedClientRepository);

		return WebClient.builder().apply(oauth2.oauth2Configuration()).build();
	}

//	@Bean
//	@Qualifier("webClient")
//	WebClient.Builder webClientBuilder() {
//		return WebClient.builder();
//	}
//
//	@Bean
//	@LoadBalanced
//	@Qualifier("loadBalancedWebClient")
//	WebClient.Builder loadBalancedWebClientBuilder() {
//		return WebClient.builder();
//	}

//	@Bean
//	@LoadBalanced
//	@Qualifier("loadBalancedWebClient")
//	public WebClient loadBalancedWebClient(OAuth2AuthorizedClientManager authorizedClientManager) {
//
//		ServletOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
//				authorizedClientManager);
//
//		return WebClient.builder().filter(oauth).build();
//	}

	@Bean
	public OAuth2AuthorizedClientManager authorizedClientManager(
			ClientRegistrationRepository clientRegistrationRepository,
			OAuth2AuthorizedClientRepository authorizedClientRepository) {

		OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
				.clientCredentials().build();

		DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
				clientRegistrationRepository, authorizedClientRepository);

		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

		return authorizedClientManager;
	}

}

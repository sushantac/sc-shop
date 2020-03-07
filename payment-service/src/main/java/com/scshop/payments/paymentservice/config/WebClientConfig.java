package com.scshop.payments.paymentservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Bean
	@Qualifier("webClient")
    WebClient.Builder webClientBuilder(){
    	return WebClient.builder();
    }
	
	@Bean
	@LoadBalanced
	@Qualifier("loadBalancedWebClient")
    WebClient.Builder loadBalancedWebClientBuilder(){
    	return WebClient.builder();
    }
    
	

	@Bean
	@LoadBalanced
	@Qualifier("loadBalancedRestTemplate")
	RestTemplate loadBalancedRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	@Qualifier("restTemplate")
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	
//    this.webClient = WebClient.builder()
//            .baseUrl(OMDB_API_BASE_URL)
//            .defaultHeader(HttpHeaders.CONTENT_TYPE, OMDB_MIME_TYPE)
//            .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)

            
}

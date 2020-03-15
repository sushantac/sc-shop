package com.scshop.security.authservice.config.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.scshop.security.authservice.config.entity.UserIdentity;
import com.scshop.security.authservice.config.entity.UserIdentityRepository;
import com.scshop.security.authservice.exception.UserRegistrationFailedException;

import reactor.core.publisher.Mono;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserIdentityRepository userIdentityRepository;


	@Autowired
	WebClient.Builder loadBalancedWebClientBuilder;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<UserIdentity> optional = userIdentityRepository.findByUsername(username);

		if (!optional.isPresent()) {
			throw new UsernameNotFoundException("Username " + username + "not found!");
		}

		return new User(username, optional.get().getPassword(), new ArrayList<>());
	}

	
	@Transactional
	public void registerUser(UserIdentity user) {

		// TODO user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		user.setPassword(user.getPassword());
		userIdentityRepository.save(user);

		// TODO: Ideally should send notification on Kafka...
		// A new user has registered..
		// User-service: create-profile, Email-service: verify email

		// For now
		// Create user profile directly
		com.scshop.application.common.model.User profileUser = new com.scshop.application.common.model.User(null,
				user.getId(), user.getFirstName(), user.getLastName(), user.getBirthDate(), user.getGender(),
				new BigDecimal(100000));

		ClientResponse response = loadBalancedWebClientBuilder.build().post()
				.uri(uriBuilder -> uriBuilder.scheme("http").host("user-service").path("/api/v1/users").build())
				.body(BodyInserters.fromPublisher(Mono.just(profileUser), com.scshop.application.common.model.User.class)).exchange().block();

		if(!HttpStatus.CREATED.equals(response.statusCode())) {
			throw new UserRegistrationFailedException("Registration failed for user - " + user.getUsername() );
		}
		
	}

	public boolean doesUserAlreadyExist(UserIdentity user) {
		return userIdentityRepository.findByUsername(user.getUsername()).isPresent();
	}

}

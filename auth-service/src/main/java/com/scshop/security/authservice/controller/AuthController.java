package com.scshop.security.authservice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scshop.security.authservice.config.entity.UserIdentity;
import com.scshop.security.authservice.config.entity.UserIdentityRepository;
import com.scshop.security.authservice.config.service.UserDetailsServiceImpl;
import com.scshop.security.authservice.exception.UserAlreadyExistsException;
import com.scshop.security.authservice.exception.UserNotFoundException;
import com.scshop.security.authservice.jwt.JwtConfig;
import com.scshop.security.authservice.model.AuthenticationRequest;
import com.scshop.security.authservice.model.AuthenticationResponse;

@RestController
@RequestMapping(path = "/api/v1/auth/")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	UserIdentityRepository userIdentityRepository;

	//TODO Need to fix this big-time... Continuing for now 
	@RequestMapping(value = "token", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtConfig.generateToken(userDetails);

		//TODO Revisit this
		Optional<UserIdentity> optional = userIdentityRepository.findByUsername(authenticationRequest.getUsername());
		
		return ResponseEntity.ok(new AuthenticationResponse(optional.get().getId(), authenticationRequest.getUsername(), token));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (BadCredentialsException e) {
			throw new UserNotFoundException("USER_NOT_FOUND");
		}
	}

	@RequestMapping(value = "signup", method = RequestMethod.POST)
	public String signUp(@RequestBody UserIdentity user) {

		boolean userAlreadyExists = userDetailsService.doesUserAlreadyExist(user);

		if (userAlreadyExists) {
			throw new UserAlreadyExistsException("USERNAME_EXISTS");
		}

		userDetailsService.registerUser(user);

		

		return user.getUsername();
	}

	@RequestMapping(value = "/test")
	public String test() {
		return "Tesk OK!";
	}

}

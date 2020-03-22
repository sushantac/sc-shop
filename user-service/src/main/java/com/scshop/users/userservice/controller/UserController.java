package com.scshop.users.userservice.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.scshop.application.common.model.User;
import com.scshop.users.userservice.entity.UserRepository;
import com.scshop.users.userservice.exception.UserNotFoundException;



@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<User> getUsers(){
		
		List<User> users = userRepository.findAll();
		
		return users;
	}
	
	/**
	 * 	
	 * @param id
	 * @return
	 */
	@RequestMapping(path = "/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public User getUser(@PathVariable UUID userId){
		
		Optional<User> optional = userRepository.findByUserId(userId);

		if (!optional.isPresent()) {
			throw new UserNotFoundException("User not found for userId: " + userId);
		}
		User user = optional.get();
		
		return user;
	}
	
	/**
	 * Create user
	 * 
	 * @param {@link User}
	 */
	@RequestMapping(path = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createUser(@RequestBody User user){
		
		User savedUser = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedUser.getUserId()).toUri();

		return ResponseEntity.created(location).build();
	}
	
	/**
	 * 
	 * @param id
	 * @param user
	 */
	@RequestMapping(path = "/{userId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser(@PathVariable UUID userId, @RequestBody User user){
		boolean is_present = userRepository.existsByUserId(userId);

		if (!is_present) {
			throw new UserNotFoundException("User not found for userId: " + userId);
		}

		user.setId(userId);
		userRepository.save(user);
	}
	
	/**
	 * //Disabling it for now: Not required - TODO 
	 * @param id
	 */
	//@RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable UUID userId) {

		boolean is_present = userRepository.existsByUserId(userId);

		if (!is_present) {
			throw new UserNotFoundException("User not found for userId: " + userId);
		}
		
		userRepository.deleteById(userId);

	}
	

}

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

import com.scshop.users.userservice.entity.User;
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
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public User getUser(@PathVariable UUID id){
		
		Optional<User> optional = userRepository.findById(id);

		if (!optional.isPresent()) {
			throw new UserNotFoundException("User not found for id: " + id);
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
				.buildAndExpand(savedUser.getId()).toUri();

		return ResponseEntity.created(location).build();
	}
	
	/**
	 * 
	 * @param id
	 * @param user
	 */
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser(@PathVariable UUID id, @RequestBody User user){
		boolean is_present = userRepository.existsById(id);

		if (!is_present) {
			throw new UserNotFoundException("User not found for id: " + id);
		}

		user.setId(id);
		userRepository.save(user);
	}
	
	/**
	 * 
	 * @param id
	 */
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable UUID id) {

		boolean is_present = userRepository.existsById(id);

		if (!is_present) {
			throw new UserNotFoundException("User not found for id: " + id);
		}
		
		userRepository.deleteById(id);

	}
	

}

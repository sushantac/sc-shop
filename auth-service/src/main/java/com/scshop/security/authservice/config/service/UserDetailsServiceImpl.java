package com.scshop.security.authservice.config.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scshop.security.authservice.config.entity.UserIdentity;
import com.scshop.security.authservice.config.entity.UserIdentityRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserIdentityRepository userIdentityRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<UserIdentity> optional = userIdentityRepository.findByUsername(username);

		if (!optional.isPresent()) {
			throw new UsernameNotFoundException("Username " + username + "not found!");
		}

		return new User(username, optional.get().getPassword(), new ArrayList<>());
	}

	public void registerUser(UserIdentity user) {

		// TODO user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		user.setPassword(user.getPassword());
		userIdentityRepository.save(user);
	}

	public boolean doesUserAlreadyExist(UserIdentity user) {
		return userIdentityRepository.findByUsername(user.getUsername()).isPresent();
	}

}

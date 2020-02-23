package com.scshop.security.authservice.config.service;

import java.util.ArrayList;

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
		
		UserIdentity userIdentity = userIdentityRepository.findByUsername(username);
		
		if(userIdentity == null) {
			throw new UsernameNotFoundException("Username " + username + "not found!");
		}
		
		return new User(username, userIdentity.getPassword(), new ArrayList<>());
	}

}

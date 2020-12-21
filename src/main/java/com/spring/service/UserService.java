package com.spring.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.entity.User;
import com.spring.security.UserCustody;

@Service
public class UserService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub

		if (!username.equals("dejvis06"))
			throw new UsernameNotFoundException("User not found by username: " + username);

		User user = new User();
		user.setUsername("dejvis06");

		return new UserCustody(user);
	}

}

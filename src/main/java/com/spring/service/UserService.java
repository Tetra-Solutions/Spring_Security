package com.spring.service;

import static com.spring.enumeration.Role.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.customException.EmailExistsException;
import com.spring.customException.UsernameExistsException;
import com.spring.entity.User;
import com.spring.security.UserCustody;

@Service
@Qualifier("userService1")
public class UserService implements UserDetailsService {

	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub

		if (!username.equals("dejvis06"))
			throw new UsernameNotFoundException("User not found by username: " + username);

		User user = new User();
		user.setUsername("dejvis06");

		return new UserCustody(user);
	}

	public User register(String firstName, String lastName, String username, String email)
			throws EmailExistsException, UsernameExistsException {

		validateUsernameAndEmail(StringUtils.EMPTY, username, email);

		String password = generatePassword();
		String encodedPassword = encodePassword(password);

		User user = new User();

		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(encodedPassword);
		user.setActive(true);
		user.setNonLocked(true);
		user.setRole(ROLE_USER.name());
		user.setAuthorities(ROLE_USER.getAuthorities());

		return null;
	}

	public User findByUsername(String username) {

		return null;
	}

	public User findByEmail(String email) {

		return null;
	}

	// create or update existing account
	private User validateUsernameAndEmail(String currentUsername, String newUsername, String newEmail)
			throws EmailExistsException, UsernameExistsException {

		if (StringUtils.isNotBlank(currentUsername)) {

			User currentUser = findByUsername(currentUsername);
			if (currentUsername == null) {
				throw new UsernameNotFoundException("No user found by username" + currentUsername);
			}

			// Validate username ,if !currentUser.getId().equals(userByUsername.getId() its
			// a new user
			User userByUsername = findByUsername(newUsername);

			if (userByUsername != null && !currentUser.getId().equals(userByUsername.getId())) {

				throw new UsernameExistsException("Username already exists");
			}

			// Validate email
			User userByEmail = findByEmail(newEmail);

			if (userByEmail != null && !currentUser.getId().equals(userByEmail.getId())) {

				throw new EmailExistsException("Username already exists");
			}
			return currentUser;
		} else {

			User userByUsername = findByUsername(newUsername);
			if (userByUsername != null) {
				throw new UsernameExistsException("Username already exists");
			}

			User userByEmail = findByEmail(newEmail);
			if (userByEmail != null) {
				throw new EmailExistsException("Username already exists");
			}
			return null;
		}
	}

	private String generatePassword() {

		return RandomStringUtils.randomAlphanumeric(10);
	}

	private String encodePassword(String password) {

		return passwordEncoder.encode(password);
	}
}

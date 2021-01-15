package com.spring.service;

import static com.spring.enumeration.Role.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

	private static final String USER_NOT_FOUND_BY_USERNAME = "User not found by username";
	private static final String EMAIL_ALREADY_EXISTS = "Email already exists";
	private static final String USERNAME_ALREADY_EXISTS = "Username already exists";
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub

		if (!username.equals("dejvis06"))
			throw new UsernameNotFoundException(USER_NOT_FOUND_BY_USERNAME + username);

		User user = new User();
		user.setUsername("dejvis06");
		user.setAuthorities(ROLE_USER.getAuthorities());

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

		return user;
	}

	public User findByUsername(String username) {

		User user = new User();
		user.setUsername("dejvis06");
		user.setPassword("password");
		user.setNonLocked(true);
		user.setAuthorities(ROLE_USER.getAuthorities());

		return user;
	}

	public User findByEmail(String email) {

		return null;
	}

	// create or update existing account
	private User validateUsernameAndEmail(String currentUsername, String newUsername, String newEmail)
			throws EmailExistsException, UsernameExistsException {

		User userByUsername = findByUsername(newUsername);
		User userByNewEmail = findByEmail(newEmail);
		User userByNewUsername = findByUsername(newUsername);

		if (StringUtils.isNotBlank(currentUsername)) {

			User currentUser = findByUsername(currentUsername);
			if (currentUsername == null) {
				throw new UsernameNotFoundException(USER_NOT_FOUND_BY_USERNAME + currentUsername);
			}

			// Validate username ,if !currentUser.getId().equals(userByUsername.getId() its
			// a new user
			if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
				throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
			}
			// Validate email
			if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
				throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
			}
			return currentUser;
		} else {

			if (userByUsername != null) {
				throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
			}
			if (userByNewEmail != null) {
				throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
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

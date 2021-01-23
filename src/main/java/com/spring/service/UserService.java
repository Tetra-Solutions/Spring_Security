package com.spring.service;

import static com.spring.enumeration.Role.*;

import java.util.concurrent.ExecutionException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

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
	private LoginAttemptService loginAttemptService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = findByUsername(username);

		if (user == null)
			throw new UsernameNotFoundException(USER_NOT_FOUND_BY_USERNAME + username);
		
		validateLoginAttempt(user);

		return new UserCustody(user);
	}

	@Transactional
	public User register(User user) throws EmailExistsException, UsernameExistsException, Exception {

		validateUsernameAndEmail(user.getUsername(), user.getEmail());

		user.setPassword(encodePassword(user.getPassword()));
		user.setActive(true);
		user.setNonLocked(true);
		user.setRole(ROLE_USER.name());
		user.setAuthorities(ROLE_USER.getAuthorities());

		return entityManager.merge(user);
	}

	public User findByUsername(String username) {

		try {
			return (User) entityManager
					.createQuery("select user from User user where user.username =:username", User.class)
					.setParameter("username", username).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public User findByEmail(String email) {

		try {
			return (User) entityManager.createQuery("select user from User user where user.email =:email", User.class)
					.setParameter("email", email).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public void validateUsername(String username) throws UsernameExistsException {

		boolean exists = entityManager.createQuery(
				"select case when user is not null then true else false end from User user where user.username =: username ",
				Boolean.class).setParameter("username", username).getSingleResult();

		if (exists)
			throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
	}

	public void validateEmail(String email) throws EmailExistsException {

		boolean exists = entityManager.createQuery(
				"select case when user is not null then true else false end from User user where user.email =: email ",
				Boolean.class).setParameter("email", email).getSingleResult();

		if (exists)
			throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
	}

	private void validateUsernameAndEmail(String username, String email)
			throws EmailExistsException, UsernameExistsException {

		validateUsername(username);
		validateEmail(email);
	}

	private String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	private void validateLoginAttempt(User user) {

		if (user.isNonLocked()) {

			if (loginAttemptService.hasExcedeedMaxNoAttempts(user.getUsername())) {
				user.setNonLocked(false);
			} else {
				user.setNonLocked(true);
			}
		}
	}
}

package com.spring.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import com.spring.service.LoginAttemptService;

@Component
public class AuthenticationFailureListener {

	@Autowired
	private LoginAttemptService loginAttemptService;

	@EventListener
	public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {

		String principal = (String) event.getAuthentication().getPrincipal();
		loginAttemptService.addToCache(principal);
	}
}

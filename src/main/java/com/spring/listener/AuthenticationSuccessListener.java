package com.spring.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.spring.security.UserCustody;
import com.spring.service.LoginAttemptService;

@Component
public class AuthenticationSuccessListener {

	@Autowired
	private LoginAttemptService loginAttemptService;

	@EventListener
	public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {

		UserCustody principal = (UserCustody) event.getAuthentication().getPrincipal();
		loginAttemptService.invalidateFromCache(principal.getUsername());
	}
}

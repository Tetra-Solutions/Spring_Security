package com.spring.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class LoginAttemptService {

	private static final int MAXIMUM_NO_ATTEMPTS = 5;

	private static final int ATTEMPT_INCREMENT = 1;

	private LoadingCache<String, Integer> loginAttemptsCache;

	public LoginAttemptService() {

		loginAttemptsCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(100)
				.build(new CacheLoader<String, Integer>() {

					public Integer load(String key) {
						return 0;
					}
				});
	}

	public void addToCache(String username) {

		try {
			int attempts = ATTEMPT_INCREMENT + loginAttemptsCache.get(username);
			loginAttemptsCache.put(username, attempts);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public boolean hasExcedeedMaxNoAttempts(String username) {

		try {

			return loginAttemptsCache.get(username) == MAXIMUM_NO_ATTEMPTS;
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void invalidateFromCache(String username) {
		try {
			loginAttemptsCache.invalidate(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

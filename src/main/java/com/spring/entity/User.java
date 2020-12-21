package com.spring.entity;

import javax.persistence.Entity;

@Entity
public class User {

	private Long id;

	private String username;

	private String password;

	private boolean isActive;

	private boolean isNonLocked;

	private String[] authorities;

	public User() {

	}

	public User(Long id, String username, String password, boolean isActive, boolean isNonLocked,
			String[] authorities) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.isActive = isActive;
		this.isNonLocked = isNonLocked;
		this.authorities = authorities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isNonLocked() {
		return isNonLocked;
	}

	public void setNonLocked(boolean isNonLocked) {
		this.isNonLocked = isNonLocked;
	}

	public String[] getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String[] authorities) {
		this.authorities = authorities;
	}

}
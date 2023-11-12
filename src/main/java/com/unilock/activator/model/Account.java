package com.unilock.activator.model;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private String username;
	private String password;
	private Boolean enabled = true;
	private Boolean credentialsexpired = false;
	private Boolean expired = false;
	private Boolean locked = false;
	private String firstname;
	private String surname;
	private Integer version;
	private String manager;
	private List<String> roles;

	public Account() {

	}

	public Account(String username, String password, Boolean enabled, Boolean credentialsexpired, Boolean expired, Boolean locked) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.credentialsexpired = credentialsexpired;
		this.expired = expired;
		this.locked = locked;
	}

	public Account(Account account) {
		setUsername(new String(account.getUsername()));
		setPassword(new String(account.getPassword()));
		setFirstname(new String(account.getFirstname()));
		setSurname(new String(account.getSurname()));
		setManager(new String(account.getManager()));
		for (String role : account.getRoles()) {
			getRoles().add(new String(role));
		}
		setVersion(account.getVersion());
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

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean isCredentialsexpired() {
		return credentialsexpired;
	}

	public void setCredentialsexpired(Boolean credentialsexpired) {
		this.credentialsexpired = credentialsexpired;
	}

	public Boolean isExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}

	public Boolean isLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<String> getRoles() {
		if (roles == null) {
			roles = new ArrayList<String>();
		}
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}

package com.project.shopping.requests;

import lombok.Data;

@Data
public class AuthRequest {

	String email;
	String userName;
	String password;
	boolean valid;

	public AuthRequest(String email, String password) {
		this.email = email;
		this.password = password;
		this.userName = email;
		this.valid = this.isValid(email, password);
	}

	private boolean isValid(String email, String password) {
		if (email == null || password == null)
			return false;
		if (email.trim().isEmpty() || password.trim().isEmpty())
			return false;
		return true;
	}

}

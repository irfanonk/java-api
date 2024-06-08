package com.project.shopping.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuthRequest {

	String email;
	String userName;
	String password;
	String firstName;
	String lastName;
	String status;
	@Schema(hidden = true)
	boolean valid;

	public AuthRequest(String email, String password, String firstName,
			String lastName) {
		this.email = email;
		this.password = password;
		this.userName = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.status = "active";
		this.valid = this.isValid(email, password);
	}

	private boolean isValid(String email, String password) {
		return email != null && !email.trim().isEmpty() && password != null && !password.trim().isEmpty();
	}

}

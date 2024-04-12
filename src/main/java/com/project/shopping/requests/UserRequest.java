package com.project.shopping.requests;

import lombok.Data;

@Data
public class UserRequest {

	String email;
	String userName;
	String password;

	public UserRequest(String email, String password) {
		this.email = email;
		this.password = password;
		this.userName = email;
	}

}

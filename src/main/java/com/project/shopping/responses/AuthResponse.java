package com.project.shopping.responses;

import lombok.Data;

@Data
public class AuthResponse {

	String message;
	String accessToken;
	String refreshToken;
}

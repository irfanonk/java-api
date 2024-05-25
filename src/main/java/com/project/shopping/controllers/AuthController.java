package com.project.shopping.controllers;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopping.entities.RefreshToken;
import com.project.shopping.entities.Role;
import com.project.shopping.entities.User;
import com.project.shopping.repos.RoleRepository;
import com.project.shopping.requests.RefreshRequest;
import com.project.shopping.requests.AuthRequest;
import com.project.shopping.responses.AuthResponse;
import com.project.shopping.responses.ErrorResponse;
import com.project.shopping.security.JwtTokenProvider;
import com.project.shopping.services.RefreshTokenService;
import com.project.shopping.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private AuthenticationManager authenticationManager;

	private JwtTokenProvider jwtTokenProvider;

	private UserService userService;

	private RoleRepository roleRepository;

	private PasswordEncoder passwordEncoder;

	private RefreshTokenService refreshTokenService;

	public AuthController(AuthenticationManager authenticationManager, UserService userService,
			PasswordEncoder passwordEncoder, RoleRepository roleRepository, JwtTokenProvider jwtTokenProvider,
			RefreshTokenService refreshTokenService) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
		this.refreshTokenService = refreshTokenService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest loginRequest) {
		AuthResponse authResponse = new AuthResponse();

		if (!loginRequest.isValid()) {
			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "invalid credentials",
					"Please provide email and password!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		User oneUser = userService.getOneUserByUserName(loginRequest.getUserName());
		System.out.println("oneUser : " + oneUser);

		if (userService.getOneUserByUserName(loginRequest.getUserName()) == null) {

			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "invalid credentials",
					"Email or password wrong!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				loginRequest.getUserName(), loginRequest.getPassword());

		Authentication auth = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwtToken = jwtTokenProvider.generateJwtToken(auth);
		User user = userService.getOneUserByUserName(loginRequest.getUserName());

		authResponse.setAccessToken("Bearer " + jwtToken);
		authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
		authResponse.setMessage("Successful");
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody AuthRequest registerRequest) {

		AuthResponse authResponse = new AuthResponse();

		if (!registerRequest.isValid()) {
			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "invalid credentials",
					"Please provide email and password!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		if (userService.getOneUserByUserName(registerRequest.getUserName()) != null) {

			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "invalid credentials",
					"Email already in use.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		User user = new User();
		user.setEmail(registerRequest.getEmail());
		user.setUserName(registerRequest.getUserName());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setFirstName(registerRequest.getFirstName());
		user.setLastName(registerRequest.getLastName());
		user.setStatus(registerRequest.getStatus());
		Role role = roleRepository.findByName("USER").get();
		user.setRoles(Collections.singletonList(role));
		userService.saveOneUser(user);

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				registerRequest.getUserName(), registerRequest.getPassword());
		Authentication auth = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwtToken = jwtTokenProvider.generateJwtToken(auth);

		authResponse.setAccessToken("Bearer " + jwtToken);
		authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
		return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
		AuthResponse response = new AuthResponse();
		RefreshToken token = refreshTokenService.getByUser(refreshRequest.getUserId());
		if (token.getToken().equals(refreshRequest.getRefreshToken()) &&
				!refreshTokenService.isRefreshExpired(token)) {

			User user = token.getUser();
			String jwtToken = jwtTokenProvider.generateJwtTokenByUserId(user.getId());
			response.setMessage("token successfully refreshed.");
			response.setAccessToken("Bearer " + jwtToken);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.setMessage("refresh token is not valid.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}

	}

}

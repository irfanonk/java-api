package com.project.shopping.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopping.dto.UpdateUserDto;
import com.project.shopping.dto.UpdateUserPasswordDto;
import com.project.shopping.entities.User;
import com.project.shopping.exceptions.UserNotFoundException;
import com.project.shopping.responses.ErrorResponse;
import com.project.shopping.responses.UserResponse;
import com.project.shopping.security.CustomHttpServletRequestWrapper;
import com.project.shopping.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	private UserService userService;
	private PasswordEncoder passwordEncoder;

	public UserController(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody User newUser) {
		User user = userService.saveOneUser(newUser);
		if (user != null)
			return new ResponseEntity<>(HttpStatus.CREATED);
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/account")
	public UserResponse getOneUserFromJwt(CustomHttpServletRequestWrapper request) {

		Long userId = (request).getUserId();
		User user = userService.getOneUserById(userId);
		if (user == null) {
			throw new UserNotFoundException();
		}
		return new UserResponse(user);
	}

	@PutMapping("/account")
	public ResponseEntity<Void> updateOneUser(CustomHttpServletRequestWrapper request,
			@RequestBody UpdateUserDto newUser) {
		Long userId = (request).getUserId();

		User user = userService.updateOneUser(userId, newUser);
		if (user != null)
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@PutMapping("/account/update-password")
	public ResponseEntity<?> updateOneUserPassword(CustomHttpServletRequestWrapper request,
			@RequestBody UpdateUserPasswordDto updateUserPasswordDto) {

		if (!updateUserPasswordDto.isValid()) {
			throw new IllegalArgumentException("Invalid password data");
		}

		Long userId = (request).getUserId();
		User user = userService.getOneUserById(userId);

		if (passwordEncoder.matches(updateUserPasswordDto.getOldPassword(), user.getPassword())) {
			user.setPassword(passwordEncoder.encode(updateUserPasswordDto.getNewPassword()));
			userService.saveOneUser(user);
			return new ResponseEntity<>(HttpStatus.OK);

		}
		ErrorResponse error = new ErrorResponse(400, "Old password is wrong", "");

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/account")
	public void deleteOneUser(CustomHttpServletRequestWrapper request) {
		Long userId = (request).getUserId();
		userService.deleteById(userId);
	}

	@GetMapping("/account/activity")
	public List<Object> getUserActivity(@PathVariable Long userId) {
		return userService.getUserActivity(userId);
	}

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
	}
}

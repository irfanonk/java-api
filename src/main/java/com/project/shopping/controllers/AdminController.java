package com.project.shopping.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.project.shopping.entities.User;
import com.project.shopping.exceptions.UserNotFoundException;
import com.project.shopping.responses.UserResponse;
import com.project.shopping.services.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private UserService userService;

	public AdminController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/users")
	public List<UserResponse> getAllUsers() {
		return userService.getAllUsers().stream()
				.map(UserResponse::new)
				.collect(Collectors.toList());
	}

	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody User newUser) {
		User user = userService.saveOneUser(newUser);
		if (user != null)
			return new ResponseEntity<>(HttpStatus.CREATED);
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/users/{userId}")
	public UserResponse getOneUser(@PathVariable Long userId) {
		User user = userService.getOneUserById(userId);
		if (user == null) {
			throw new UserNotFoundException();
		}
		return new UserResponse(user);
	}

	@PutMapping("/users/{userId}")
	public ResponseEntity<Void> updateOneUser(@PathVariable Long userId, @RequestBody User newUser) {
		User user = userService.updateOneUser(userId, newUser);
		if (user != null)
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@DeleteMapping("/users/{userId}")
	public void deleteOneUser(@PathVariable Long userId) {
		userService.deleteById(userId);
	}

	@GetMapping("/users/activity/{userId}")
	public List<Object> getUserActivity(@PathVariable Long userId) {
		return userService.getUserActivity(userId);
	}

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
	}
}

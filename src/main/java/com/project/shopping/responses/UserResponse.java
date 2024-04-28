package com.project.shopping.responses;

import java.util.List;

import com.project.shopping.entities.Role;
import com.project.shopping.entities.User;

import lombok.Data;

@Data
public class UserResponse {

	Long id;
	int avatarId;
	String userName;
	List<Role> roles;

	public UserResponse(User entity) {
		this.id = entity.getId();
		this.avatarId = entity.getAvatar();
		this.userName = entity.getUserName();
		this.roles = entity.getRoles();
	}
}

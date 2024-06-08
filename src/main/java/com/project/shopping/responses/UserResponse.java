package com.project.shopping.responses;

import java.util.List;

import com.project.shopping.entities.Role;
import com.project.shopping.entities.User;

import lombok.Data;

@Data
public class UserResponse {

	private Long id;
	private int avatarId;
	private String email;
	private String userName;
	private String firstName;
	private String lastName;
	private String country;
	private String city;
	private String phone;
	private String status;
	private String profession;
	private String profileUrl;
	private List<Role> roles;

	public UserResponse(User entity) {
		this.id = entity.getId();
		this.avatarId = entity.getAvatar();
		this.email = entity.getEmail();
		this.userName = entity.getUserName();
		this.firstName = entity.getFirstName();
		this.lastName = entity.getLastName();
		this.country = entity.getCountry();
		this.city = entity.getCity();
		this.phone = entity.getPhone();
		this.status = entity.getStatus();
		this.profession = entity.getProfession();
		this.roles = entity.getRoles();
		this.profileUrl = entity.getProfileUrl();
	}
}

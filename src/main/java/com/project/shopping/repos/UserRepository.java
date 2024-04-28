package com.project.shopping.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shopping.entities.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

	User findByUserName(String userName);

	Boolean existsByUserName(String userName);

}

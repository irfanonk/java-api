package com.project.shopping.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shopping.entities.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	RefreshToken findByUserId(Long userId);

}

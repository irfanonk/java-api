package com.project.shopping.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shopping.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}

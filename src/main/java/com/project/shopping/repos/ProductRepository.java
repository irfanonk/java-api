package com.project.shopping.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.shopping.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

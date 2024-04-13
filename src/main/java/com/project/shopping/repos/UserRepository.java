package com.project.shopping.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.shopping.entities.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

	User findByUserName(String userName);

	// @Query(value = "SELECT :fields FROM users u WHERE u.id = :userId",
	// nativeQuery = true)
	// User findUserByUserId(@Param("userId") Long userId, @Param("fields") String
	// fields);

	// default User findUserByUserIdWithFields(Long userId, List<String> fieldList)
	// {
	// System.out.println("fieldList = " + fieldList);

	// String fields = String.join(", ", fieldList);
	// System.out.println("fields = " + fields);
	// return findUserByUserId(userId, fields);
	// }

}

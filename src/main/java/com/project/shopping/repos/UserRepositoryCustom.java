package com.project.shopping.repos;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

import com.project.shopping.entities.User;

@NoRepositoryBean
public interface UserRepositoryCustom {
    User findUserByUserIdWithFields(Long userId, List<String> fieldList);
}

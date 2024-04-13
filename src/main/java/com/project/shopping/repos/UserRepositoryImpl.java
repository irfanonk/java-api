package com.project.shopping.repos;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import com.project.shopping.entities.User;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final EntityManager entityManager;

    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User findUserByUserIdWithFields(Long userId, List<String> fieldList) {
        System.out.println("fieldList = " + fieldList);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        // Create a list of selections for the specified fields
        List<javax.persistence.criteria.Selection<?>> selections = fieldList.stream()
                .map(field -> root.get(field))
                .collect(Collectors.toList());

        query.select(cb.construct(User.class, selections.toArray(new javax.persistence.criteria.Selection[0])))
                .where(cb.equal(root.get("id"), userId));
        // Execute the query and retrieve the result
        User result = entityManager.createQuery(query).getSingleResult();
        System.out.println("result = " + result);
        return result;
    }
}

package com.spring;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.spring.entity.User;

@SpringBootTest
class SpringSecurityApplicationTests {

	@PersistenceContext
	EntityManager entityManager;

	@Test
	@Transactional
	@Rollback(false)
	void contextLoads() {
		try {
			entityManager.createQuery(
					"select user.username case when user is not null then 'true' else 'false' from User user where user.username =: username ",
					Boolean.class).setParameter("username", "dejvis06").getSingleResult();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}

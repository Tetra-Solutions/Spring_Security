package com.spring;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringSecurityApplicationTests {

	@PersistenceContext
	EntityManager e;

	@Test
	void contextLoads() {
		System.out.println(org.hibernate.Version.getVersionString());
	}

}

package com.bookstore;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.entity.Employee;

@SpringBootTest
class DBTesting{
	
	@Autowired
	private EntityManager entityManager;
	
	@Test
	void seeIfEmplUserConnectionTableWorks() {
		Query theQuery = entityManager.createNativeQuery("select e.* FROM employee e inner join client_connector c on c.user_id_user=e.emplid", Employee.class);
		System.out.println(theQuery.getSingleResult());
	}

	
}

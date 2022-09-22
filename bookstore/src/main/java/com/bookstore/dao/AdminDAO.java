package com.bookstore.dao;


import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.entity.Employee;
import com.bookstore.entity.User;


@Repository
public class AdminDAO {

	private EntityManager entityManager;
	
	@Autowired
	public AdminDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional
	public User getUserByUsername(String username) {
		
		User user = new User();
		
		Query theQuery = entityManager.createQuery("FROM users u WHERE u.username = :u", User.class);
		theQuery.setParameter("u", username);
		
		user = (User) theQuery.getSingleResult();
		
		System.out.println("ADMIN========="+user);
		
		return user;
	}
	
	@Transactional
	public void checker() {
		
		Query theQuery = entityManager.createNativeQuery("select e.* FROM employee e inner join client_connector c on c.user_id_user=e.emplid", Employee.class);
		System.out.println(theQuery.getSingleResult());
	}
	
	
}
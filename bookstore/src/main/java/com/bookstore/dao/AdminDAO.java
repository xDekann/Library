package com.bookstore.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.entity.Authority;
import com.bookstore.entity.Employee;
import com.bookstore.entity.User;


@Repository
public class AdminDAO {

	private EntityManager entityManager;
	
	@Autowired
	public AdminDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	// selects and getters
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
	public List<Authority> getAllAuthorities() {
		Query theQuery = entityManager.createNativeQuery("select * from authorities", Authority.class);
		List<Authority> authorities = theQuery.getResultList();
		return authorities;
	}
	
	@Transactional
	public Authority getSingleAuthority(String name) {
		Query theQuery = entityManager.createQuery("from authorities a where a.authorityName=:aname ", Authority.class);
		theQuery.setParameter("aname", name);
		
		return (Authority) theQuery.getSingleResult();
	}
	
	
	// creations
	// additions
	
	@Transactional
	public void addUser(User user) {
		User dbUser = entityManager.merge(user);
		user.setId(dbUser.getId());
	}
	
}
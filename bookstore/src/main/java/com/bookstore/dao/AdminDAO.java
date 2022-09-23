package com.bookstore.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.entity.Author;
import com.bookstore.entity.Authority;
import com.bookstore.entity.Book;
import com.bookstore.entity.Employee;
import com.bookstore.entity.User;
import com.bookstore.security.BookUserDetailsService;


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
		Query theQuery = entityManager.createQuery("select distinct u from users u inner join fetch u.roles WHERE u.username = :u", User.class);
		theQuery.setParameter("u", username);
		user = (User) theQuery.getSingleResult();
		return user;
	}
	
	@Transactional
	public List<Authority> getAllAuthorities() {
		Query theQuery = entityManager.createQuery("from authorities", Authority.class);
		List<Authority> authorities = theQuery.getResultList();
		return authorities;
	}
	
	@Transactional
	public Authority getAuthority(String name) {
		Query theQuery = entityManager.createQuery("from authorities a where a.authorityName=:aname ", Authority.class);
		theQuery.setParameter("aname", name);
		
		return (Authority) theQuery.getSingleResult();
	}
	
	@Transactional
	public List<Author> getAllAuthors(){
		Query theQuery = entityManager.createQuery("from author a left join fetch a.books", Author.class);
		List<Author> authors = theQuery.getResultList();
		return authors;
	}
	@Transactional
	public Author getAuthor(int id) {
		Query theQuery = entityManager.createQuery("from author a where a.id=:theId");
		theQuery.setParameter("theId", id);
		return (Author) theQuery.getSingleResult();
	}
	
	// creations
	// additions (merge = add/update)
	@Transactional
	public void addUser(User user) {
		User dbUser = entityManager.merge(user);
		user.setId(dbUser.getId());
	}
	@Transactional 
	public void addEmployee(Employee empl){
		Employee dbEmployee = entityManager.merge(empl);
		empl.setId(dbEmployee.getId());
		
	}
	@Transactional
	public void addBook(Book book) {
		Book dbBook = entityManager.merge(book);
		book.setIsbn(book.getId());
	}
	@Transactional
	public void addAuthor(Author author) {
		Author dbAuthor = entityManager.merge(author);
		author.setId(author.getId());
	}
}
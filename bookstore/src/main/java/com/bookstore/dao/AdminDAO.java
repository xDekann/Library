package com.bookstore.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.context.Theme;

import com.bookstore.entity.Author;
import com.bookstore.entity.Authority;
import com.bookstore.entity.Book;
import com.bookstore.entity.BookCopy;
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
		Query theQuery = entityManager.createQuery("select distinct a from author a left join fetch a.books", Author.class);
		List<Author> authors = theQuery.getResultList();
		return authors;
	}
	@Transactional
	public Author getAuthor(int id) {
		Query theQuery = entityManager.createQuery("from author a where a.id=:theId");
		theQuery.setParameter("theId", id);
		return (Author) theQuery.getSingleResult();
	}
	@Transactional
	public List<Book> getAuthorBooks(int id) {
		Query theQuery = entityManager.createQuery("select distinct b from book b inner join fetch b.authors a where a.id=: theid",Book.class);
		theQuery.setParameter("theid", id);
		List<Book> books = theQuery.getResultList();
		return books;
	}
	@Transactional
	public List<Employee> getAllEmployees(){
		Query theQuery = entityManager.createQuery("from employee", Employee.class);
		List<Employee> employees = theQuery.getResultList();
		return employees;
	}
	@Transactional
	public Employee getEmployee(int id) {
		Query theQuery = entityManager.createQuery("from employee a where a.id=:theId");
		theQuery.setParameter("theId", id);
		return (Employee) theQuery.getSingleResult();
	}
	
	@Transactional
	public List<Book> getAllBooks() {
		Query theQuery = entityManager.createNativeQuery("select distinct b.* from book b left join book_copy bc on b.bookid=bc.fk_book", Book.class);
		List<Book> books = theQuery.getResultList();
		return books;
	}
	@Transactional
	public List<Book> getBooksByName(String name){
		Query theQuery = entityManager.createNativeQuery("select distinct b.* from book b left join book_copy bc on b.bookid=bc.fk_book where b.title=:nameS", Book.class);
		theQuery.setParameter("nameS", name);
		List<Book> books = theQuery.getResultList();
		return books;
	}
	@Transactional
	public Book getBookById(int id) {
		Query theQuery = entityManager.createNativeQuery("select distinct b.* from book b where b.bookid=:theid", Book.class);
		theQuery.setParameter("theid", id);
		return (Book) theQuery.getSingleResult();
		
	}
	@Transactional
	public BookCopy getBookCopyById(int id) {
		BookCopy bookCopy = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select distinct bc.* from book_copy bc where bc.fk_book=:theid limit 1", BookCopy.class);
			theQuery.setParameter("theid", id);
			bookCopy = (BookCopy) theQuery.getSingleResult();
		}catch(NoResultException nr) {
			
		}
		return bookCopy;
		
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
		book.setIsbn(dbBook.getId());
	}
	@Transactional
	public void addAuthor(Author author) {
		Author dbAuthor = entityManager.merge(author);
		author.setId(dbAuthor.getId());
	}
	@Transactional
	public void addBookCopy(BookCopy bookCopy) {
		BookCopy dBookCopy = entityManager.merge(bookCopy);
		bookCopy.setId(dBookCopy.getId());
	}
	// deletion
	@Transactional
	public void deleteEmployee(int id) {
		Query theQuery = entityManager.createQuery("delete from employee e where e.id=:theid");
		theQuery.setParameter("theid", id);
		theQuery.executeUpdate();
	}
	@Transactional
	public void deleteBookCopy(int id) {
		Query theQuery = entityManager.createQuery("delete from book_copy bc where bc.id=:theid");
		theQuery.setParameter("theid", id);
		theQuery.executeUpdate();
	}
}
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
import com.bookstore.entity.BookRent;
import com.bookstore.entity.Client;
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
	public List<User> getAllUsers(){
		List<User> users = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select distinct u.* from users u", User.class);
			users = theQuery.getResultList();
		}catch(NoResultException nr) {
			
		}
		return users;
	}
	
	@Transactional
	public User getUserByUsername(String username) {
		User user = null;
		Query theQuery = entityManager.createQuery("select distinct u from users u inner join fetch u.roles WHERE u.username = :u", User.class);
		theQuery.setParameter("u", username);
		user = (User) theQuery.getSingleResult();
		return user;
	}
	
	@Transactional
	public User getUserById(int id) {
		User user = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select distinct u.* from users u WHERE u.id = :theid", User.class);
			theQuery.setParameter("theid", id);
			user = (User) theQuery.getSingleResult();
			
		}catch(NoResultException nr) {
			
		}
		return user;
	}
	
	@Transactional
	public List<User> getUsersByUsername(String username) {
		List<User> users = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select distinct u.* from users u WHERE u.username = :theusername", User.class);
			theQuery.setParameter("theusername", username);
			users = theQuery.getResultList();
		}catch(NoResultException nr) {
			
		}
		return users;
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
	public Author getAuthorByDetails(String name, String surname) {
		Author author = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select distinct a.* from author a where a.name=:thename and a.surname=:thesurname",Author.class);
			theQuery.setParameter("thename", name);
			theQuery.setParameter("thesurname", surname);
			author = (Author) theQuery.getSingleResult();
		}catch (NoResultException nr) {
			
		}
		return author;	
	}
	
	@Transactional
	public List<Author> getAuthorsBySurname(String surname) {
		List<Author> authors = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select distinct a.* from author a left join author_connector ac on a.authorid=ac.author_idC where a.surname=:thesurname",Author.class);
			theQuery.setParameter("thesurname", surname);
			authors = theQuery.getResultList();
		}catch (NoResultException nr) {
			
		}
		return authors;	
	}
	
	@Transactional
	public List<Employee> getEmployeesBySurname(String surname) {
		List<Employee> employees = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select distinct e.* from employee e left join employee_connector ec on e.emplid = ec.user_id_user where e.surname=:thesurname",Employee.class);
			theQuery.setParameter("thesurname", surname);
			employees = theQuery.getResultList();
		}catch (NoResultException nr) {
			
		}
		return employees;	
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
	public BookCopy getBookCopyByBookId(int id) {
		BookCopy bookCopy = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select distinct bc.* from book_copy bc where bc.fk_book=:theid limit 1", BookCopy.class);
			theQuery.setParameter("theid", id);
			bookCopy = (BookCopy) theQuery.getSingleResult();
		}catch(NoResultException nr) {
			
		}
		return bookCopy;	
	}
	@Transactional
	public BookCopy getBookCopyById(int id) {
		BookCopy bookCopy = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select distinct bc.* from book_copy bc where bc.copyid=:theid limit 1", BookCopy.class);
			theQuery.setParameter("theid", id);
			bookCopy = (BookCopy) theQuery.getSingleResult();
		}catch(NoResultException nr) {
			
		}
		return bookCopy;	
	}
	@Transactional
	public List<Client> getAllClients() {
		List<Client> clients = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select distinct c.* from client c left join book_rent br on c.clientid=br.clientidR", Client.class);
			clients = theQuery.getResultList();
		}catch(NoResultException nr) {
			
		}
		return clients;
	}
	@Transactional
	public List<Client> getClientsByEmail(String email){
		List<Client> clients = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select distinct c.* from client c where c.email=:theemail", Client.class);
			theQuery.setParameter("theemail", email);
			clients = theQuery.getResultList();
		}catch(NoResultException nr) {
			
		}
		return clients;
	}
	@Transactional
	public Client getClientById(int id){
		Client client = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select distinct c.* from client c where c.clientid=:theid", Client.class);
			theQuery.setParameter("theid", id);
			client = (Client) theQuery.getSingleResult();
		}catch(NoResultException nr) {
			
		}
		return client;
	}
	@Transactional
	public List<BookRent> getClientRents(int id){
		List<BookRent> rents = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select distinct br.* from book_rent br where br.clientidR=:theid",BookRent.class);
			theQuery.setParameter("theid", id);
			rents = theQuery.getResultList();
		}catch(NoResultException nr) {
			
		}
		return rents;
	}
	@Transactional
	public List<BookCopy> getCopiesForRent(String title, String ph, String yop){
		List<BookCopy> copies = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select bc.* from book_copy bc"
					+ " inner join book b on bc.fk_book = b.bookid"
					+ " where b.title=:thetitle and b.publishing_house=:theph"
					+ " and b.year_of_publishment=:yop"
					+ " and bc.status=true",BookCopy.class);	
			theQuery.setParameter("thetitle", title);
			theQuery.setParameter("theph", ph);
			theQuery.setParameter("yop", yop);
			copies = theQuery.getResultList();
		}catch(NoResultException nr) {
			
		}
		return copies;
	}
	@Transactional
	public BookRent getRentById(int id) {
		BookRent rent = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select distinct br.* from book_rent br where br.rentid=:theid", BookRent.class);
			theQuery.setParameter("theid", id);
			rent = (BookRent) theQuery.getSingleResult();
			
		}catch(NoResultException nr) {
			
		}
		return rent;
	}
	@Transactional
	public BookCopy getRentParentCopy(int id){
		BookCopy copy = null;
		try {
			Query theQuery = entityManager.createNativeQuery("select distinct bc.* from book_copy bc inner join book_rent br on bc.copyid=br.copyidR where br.rentid=:theid" , BookCopy.class);
			theQuery.setParameter("theid", id);
			copy = (BookCopy) theQuery.getSingleResult();
		}catch(NoResultException nr) {
			
		}
		return copy;
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
	@Transactional
	public void addClient(Client client) {
		Client dbClient = entityManager.merge(client);
		client.setId(dbClient.getId());
	}
	@Transactional
	public void addRent(BookRent rent) {
		BookRent dbRent = entityManager.merge(rent);
		rent.setId(dbRent.getId());
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
	@Transactional
	public void deleteUser(int id) {
		Query theQuery = entityManager.createNativeQuery("delete from users u where u.id=:theid");
		theQuery.setParameter("theid", id);
		theQuery.executeUpdate();
	}
	@Transactional
	public void deleteClient(int id) {
		Query theQuery = entityManager.createNativeQuery("delete from client c where c.clientid=:theid");
		theQuery.setParameter("theid", id);
		theQuery.executeUpdate();
	}
}
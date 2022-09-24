package com.bookstore.controller;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.dao.AdminDAO;
import com.bookstore.entity.Author;
import com.bookstore.entity.Authority;
import com.bookstore.entity.Book;
import com.bookstore.entity.BookCopy;
import com.bookstore.entity.Employee;
import com.bookstore.entity.User;


@Controller
@RequestMapping("/admins")
public class AdminController {
	
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private PasswordEncoder encoder;
	
	@GetMapping("/start")
	public String showStartingPage() {
		return "admin/admin";
	}
	
	// creations
	// user creation
	
	
	
	@GetMapping("creation/user/form")
	public String createUserForm(Model theModel) {
		
		User user = new User();
		user.setEnabled(true);
		List<Authority> auths = adminDAO.getAllAuthorities();
		
		theModel.addAttribute("user", user);
		theModel.addAttribute("auths", auths);
		
		return "admin/user-form";
	}
	@PostMapping("creation/user/creation")
	public String createUser(@ModelAttribute("user") User user, @RequestParam("auths") List<String> roles) {
		// encode password
		user.setPassword(encoder.encode(user.getPassword()));
		// add user roles
		roles.forEach(role->{
			user.addAuthority(adminDAO.getAuthority(role));
		});
		adminDAO.addUser(user);
		return "redirect:/admins/show/users/get";
	}
	@GetMapping("creation/employee/form")
	public String createEmployeeForm(Model theModel) {
		Employee employee = new Employee();
		theModel.addAttribute("empl",employee);
		return "admin/emp-form";
	}
	@PostMapping("creation/employee/creation")
	public String createEmployee(@ModelAttribute("empl") Employee employee) {
		adminDAO.addEmployee(employee);
		return "redirect:/admins/show/employees/get";
	}
	@GetMapping("creation/author/form")
	public String createAuthorForm(Model theModel) {
		Author author = new Author();
		theModel.addAttribute("author", author);
		return "admin/author-form";
		
	}
	@PostMapping("creation/author/creation")
	public String createAuthor(@ModelAttribute("author") Author author) {
		adminDAO.addAuthor(author);
		return "redirect:/admins/start";
	}
	
	/*
	@GetMapping("creation/book/form") 
	public String createBookForm(@RequestParam("authorId") int id, Model theModel) {
		
		Author author = adminDAO.getAuthor(id);
		System.out.println(author.getSurname());
		Book book = new Book();
		
		theModel.addAttribute("author", author);
		theModel.addAttribute("book",book);
		
		return "employee/book-creation";
	}
	*/
	@GetMapping("creation/book/form") 
	public String createBookForm(Model theModel) {
		
		Book book = new Book();
		theModel.addAttribute("book",book);
		
		return "employee/book-form";
	}
	
	@PostMapping("creation/book/creation") //
	public String createBook(@RequestParam(value="nicknames") String authors, 
			@ModelAttribute("book") Book book) {
		
		//Author author = adminDAO.getAuthor(authorId);
		//author.addBook(book);
		//adminDAO.addBook(book);
		
		//authors.forEach(author->System.out.println(author));
		
		// getting all authors name+surname into separate strings
		List<String> authorsObtained = Arrays.asList(authors.split(", "));
		List<String> separateDetails = null;
		String name;
		String surname;
		Author dbAuthor;
		
		for(String author : authorsObtained) {
			// getting one author's name in one and surname in second string
			separateDetails = Arrays.asList(author.split(" "));
			name = separateDetails.get(0);
			surname = separateDetails.get(1);
			System.out.println(name+" "+surname);
			dbAuthor = adminDAO.getAuthorByDetails(name, surname);
			if(dbAuthor == null) {
				dbAuthor = new Author();
				dbAuthor.setName(name);
				dbAuthor.setSurname(surname);
				dbAuthor.addBook(book);
				adminDAO.addAuthor(dbAuthor);
			}else {
				dbAuthor.addBook(book);
			}
			dbAuthor=null;
		}
		
		adminDAO.addBook(book);
		
		return "redirect:/admins/show/authors/get";
	}
	@GetMapping("creation/book/creation/add/quantity")
	public String addBookQuantity(@RequestParam ("bookId") int id, Model theModel) {
		
		Book book = adminDAO.getBookById(id);
		BookCopy bookCopy = new BookCopy();
		
		bookCopy.setFkBook(id);
		bookCopy.setIsbn(book.getIsbn());
		bookCopy.setStatus(true);
		
		adminDAO.addBookCopy(bookCopy);
		//book.addCopy(bookCopy);
		
		theModel.addAttribute("books", adminDAO.getAllBooks());
		
		return "employee/pure-books";
	}
	
	// select/show
	
	@GetMapping("show/users/get")
	public String showAllUsers(Model theModel) {
		theModel.addAttribute("users",adminDAO.getAllUsers());
		return "admin/show-users";
	}
	
	@GetMapping("show/authors/get")
	public String showAllAuthors(Model theModel) {
		theModel.addAttribute("authors", adminDAO.getAllAuthors());
		return "employee/show-authors";
	}
	
	
	@GetMapping("show/book/showall") // author list related
	public String showAllAuthorBooks(@RequestParam("authorId") int id, Model theModel) {
		
		theModel.addAttribute("books", adminDAO.getAuthorBooks(id));
		theModel.addAttribute("author", adminDAO.getAuthor(id));
		
		return "employee/author-books";
		
	}
	@GetMapping("show/employees/get")
	public String showAllEmployees(Model theModel) {
		
		theModel.addAttribute("employees", adminDAO.getAllEmployees());
		
		return "admin/show-emps";
	}
	
	@GetMapping("show/books/get") // pure book show
	public String showAllBooks(Model theModel) {
		
		List<Book> allBooks = adminDAO.getAllBooks();
		
		theModel.addAttribute("books", allBooks);
		
		String bookTitleForSearch="";
		theModel.addAttribute("titleS", bookTitleForSearch);
		// state of pure-books.html span
		theModel.addAttribute("quantity", new String("0"));
		
		return "employee/pure-books";
	}
	
	@GetMapping("show/books/get/viasearch")
	public String showCertainBooks(@RequestParam (value="titleS",required = false) String name, Model theModel) {
		theModel.addAttribute("books",adminDAO.getBooksByName(name));
		// state of pure-books.html span
		theModel.addAttribute("quantity", new String("0"));
		return "employee/pure-books";
	}
	
	@GetMapping("show/authors/get/viasearch")
	public String showCertainAuthors(@RequestParam (value="authorS", required=false) String surname, Model theModel) {
		theModel.addAttribute("authors",adminDAO.getAuthorsByName(surname));
		return "employee/show-authors";
	}
	
	@GetMapping("show/users/get/viasearch")
	public String showCertainUsers(@RequestParam (value="userS", required=false) String username, Model theModel) {
		theModel.addAttribute("users",adminDAO.getUsersByUsername(username));
		return "admin/show-users";
	}
	
	// update
	@GetMapping("update/employee/form")
	public String updateEmployee(@RequestParam("emplId") int id, Model theModel) {
		
		theModel.addAttribute("empl", adminDAO.getEmployee(id));
		
		return "admin/emp-form";
	}
	
	@GetMapping("update/user/disable")
	public String disableUser(@RequestParam("userId") int id) {
		
		User user = adminDAO.getUserById(id);
		user.setEnabled(false);
		adminDAO.addUser(user);
		return "redirect:/admins/show/users/get";
	}
	
	@GetMapping("update/user/enable")
	public String enableUser(@RequestParam("userId") int id) {
		
		User user = adminDAO.getUserById(id);
		user.setEnabled(true);
		adminDAO.addUser(user);
		return "redirect:/admins/show/users/get";
	}
	
	@GetMapping("update/user/update")
	public String updateUser(@RequestParam("userId") int id, Model theModel) {
		User user = adminDAO.getUserById(id);
		List<Authority> auths = adminDAO.getAllAuthorities();
		
		theModel.addAttribute("user",user);
		theModel.addAttribute("auths", auths);
		
		return "admin/user-form";
	}
	
	// delete
	@GetMapping("delete/employee")
	public String deleteEmployee(@RequestParam ("emplId") int id) {
		adminDAO.deleteEmployee(id);
		return "redirect:/admins/show/employees/get";
	}
	
	@GetMapping("deletion/book/deletion/del/quantity")
	public String deleteBookQuantity(@RequestParam ("bookId") int id, Model theModel) {
		
		Book book = adminDAO.getBookById(id);
		BookCopy bookCopy = adminDAO.getBookCopyById(book.getId());
		
		if(bookCopy!=null) {
			adminDAO.deleteBookCopy(bookCopy.getId());
			book.getCopies().remove(bookCopy);
		}
		
		theModel.addAttribute("books", adminDAO.getAllBooks());
		
		return "employee/pure-books";
	}
	
	// other
	@GetMapping("show/books/get/available")
	public String getBookAvailability(@RequestParam("bookId") int id, Model theModel) {
		
		
		theModel.addAttribute("books", adminDAO.getAllBooks());
		
		int quantityVal=0;
		Book checkForAvailableCopies = adminDAO.getBookById(id);
		List<BookCopy> copies = checkForAvailableCopies.getCopies();
		
		for(BookCopy copy : copies) {
			if(copy.isStatus()==true) quantityVal++;
		}
		
		// state of pure-books.html span
		if(quantityVal==0) theModel.addAttribute("quantity", new String("0"));
		else 			theModel.addAttribute("quantity", new String("1"));
		
		theModel.addAttribute("quantityVal",quantityVal);
		
		return "employee/pure-books";
		
	}
	
}

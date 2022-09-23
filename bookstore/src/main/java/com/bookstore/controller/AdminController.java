package com.bookstore.controller;


import java.util.List;

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
		return "redirect:/admins/start";
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
		return "redirect:/admins/start";
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
	
	@GetMapping("creation/book/form")
	public String createBook(@RequestParam("authorId") int id, Model theModel) {
		
		Author author = adminDAO.getAuthor(id);
		System.out.println(author.getSurname());
		Book book = new Book();
		
		theModel.addAttribute("author", author);
		theModel.addAttribute("book",book);
		
		return "employee/book-creation";
	}
	@PostMapping("creation/book/creation")
	public String createBook(@RequestParam("authId") int authorId, 
			@ModelAttribute("book") Book book) {
		
		Author author = adminDAO.getAuthor(authorId);
		author.addBook(book);
		adminDAO.addAuthor(author);
		
		
		return "redirect:/admins/show/authors/get";
	}
	
	// select/show
	@GetMapping("show/authors/get")
	public String showAllAuthors(Model theModel) {
		
		theModel.addAttribute("authors", adminDAO.getAllAuthors());
		return "employee/show-authors";
	}
	
	
}

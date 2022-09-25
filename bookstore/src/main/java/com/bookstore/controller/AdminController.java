package com.bookstore.controller;


import java.lang.reflect.Array;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.time.temporal.ChronoUnit;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookstore.dao.AdminDAO;
import com.bookstore.entity.Author;
import com.bookstore.entity.Authority;
import com.bookstore.entity.Book;
import com.bookstore.entity.BookCopy;
import com.bookstore.entity.BookRent;
import com.bookstore.entity.Client;
import com.bookstore.entity.Employee;
import com.bookstore.entity.User;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import ch.qos.logback.classic.pattern.Util;


@Controller
@RequestMapping("/admins")
public class AdminController {
	
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private PasswordEncoder encoder;
	
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
		
		// add account for newly created employee
		// temporarily disabled - need configuration by admin!
		User user = new User();
		user.setEnabled(false);
		user.setUsername(employee.getName()+" "+employee.getSurname());
		user.setPassword(encoder.encode(employee.getName()+" "+employee.getSurname()));
		
		// add to database
		//adminDAO.addUser(user);
		//adminDAO.addEmployee(employee);
		
		// link user to account and vice versa
		employee.addUser(user);
		user.addEmployee(employee);
		
		// update the link
		//adminDAO.addUser(user);
		adminDAO.addEmployee(employee);
		
		return "redirect:/admins/show/employees/get";
	}

	// select/show
	
	@GetMapping("show/users/get")
	public String showAllUsers(Model theModel) {
		theModel.addAttribute("users",adminDAO.getAllUsers());
		return "admin/show-users";
	}
	
	@GetMapping("show/employees/get")
	public String showAllEmployees(Model theModel) {
		
		theModel.addAttribute("employees", adminDAO.getAllEmployees());
		
		return "admin/show-emps";
	}
	
	@GetMapping("show/users/get/viasearch")
	public String showCertainUsers(@RequestParam (value="userS", required=false) String username, Model theModel) {
		theModel.addAttribute("users",adminDAO.getUsersByUsername(username));
		return "admin/show-users";
	}
	
	@GetMapping("show/employees/get/viasearch")
	public String showCertainEmployees(@RequestParam (value="emplS", required=false) String surname, Model theModel) {
		theModel.addAttribute("employees",adminDAO.getEmployeesBySurname(surname));
		return "admin/show-emps";
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
	
	@GetMapping("delete/user/delete")
	public String deleteUser(@RequestParam("userId") int id) {
		adminDAO.deleteUser(id);
		
		return "redirect:/admins/show/users/get";
	}
	
	
}

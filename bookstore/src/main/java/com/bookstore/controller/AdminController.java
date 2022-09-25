package com.bookstore.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.bookstore.dao.LibraryDAO;
import com.bookstore.entity.Authority;
import com.bookstore.entity.Employee;
import com.bookstore.entity.User;

@Controller
@RequestMapping("/admins")
public class AdminController {
	
	@Autowired
	private LibraryDAO libraryDAO;
	@Autowired
	private PasswordEncoder encoder;
	
	// creations
	// user creation
	@GetMapping("creation/user/form")
	public String createUserForm(Model theModel) {
		
		User user = new User();
		user.setEnabled(true);
		List<Authority> auths = libraryDAO.getAllAuthorities();
		
		theModel.addAttribute("user", user);
		theModel.addAttribute("auths", auths);
		
		return "admin/user-form";
	}
	@PostMapping("creation/user/creation")
	public String createUser(@Valid @ModelAttribute("user") User user,
			@RequestParam("auths") List<String> roles) {
		
		// encode password
		user.setPassword(encoder.encode(user.getPassword()));
		// add user roles
		roles.forEach(role->{
			user.addAuthority(libraryDAO.getAuthority(role));
		});
		libraryDAO.addUser(user);
		return "redirect:/admins/show/users/get";
	}
	@GetMapping("creation/employee/form")
	public String createEmployeeForm(Model theModel) {
		Employee employee = new Employee();
		theModel.addAttribute("empl",employee);
		return "admin/emp-form";
	}
	@PostMapping("creation/employee/creation")
	public String createEmployee(@Valid @ModelAttribute("empl") Employee employee,
								 BindingResult bs) {
		
		if(bs.hasErrors()) return "admin/emp-form";
		// add account for newly created employee
		// temporarily disabled - need configuration by admin!
		User user = new User();
		user.setEnabled(false);
		user.setUsername(employee.getName()+" "+employee.getSurname());
		user.setPassword(encoder.encode(employee.getName()+" "+employee.getSurname()));
		
		// link user to account and vice versa
		employee.addUser(user);
		user.addEmployee(employee);
		
		libraryDAO.addEmployee(employee);
		
		return "redirect:/admins/show/employees/get";
	}

	// select/show
	@GetMapping("show/users/get")
	public String showAllUsers(Model theModel) {
		theModel.addAttribute("users",libraryDAO.getAllUsers());
		return "admin/show-users";
	}
	
	@GetMapping("show/employees/get")
	public String showAllEmployees(Model theModel) {
		
		theModel.addAttribute("employees", libraryDAO.getAllEmployees());
		
		return "admin/show-emps";
	}
	
	@GetMapping("show/users/get/viasearch")
	public String showCertainUsers(@RequestParam (value="userS", required=false) String username, Model theModel) {
		theModel.addAttribute("users",libraryDAO.getUsersByUsername(username));
		return "admin/show-users";
	}
	
	@GetMapping("show/employees/get/viasearch")
	public String showCertainEmployees(@RequestParam (value="emplS", required=false) String surname, Model theModel) {
		theModel.addAttribute("employees",libraryDAO.getEmployeesBySurname(surname));
		return "admin/show-emps";
	}
	
	// update
	@GetMapping("update/employee/form")
	public String updateEmployee(@RequestParam("emplId") int id, Model theModel) {
		
		theModel.addAttribute("empl", libraryDAO.getEmployee(id));
		
		return "admin/emp-form";
	}
	
	@GetMapping("update/user/disable")
	public String disableUser(@RequestParam("userId") int id) {
		
		User user = libraryDAO.getUserById(id);
		user.setEnabled(false);
		libraryDAO.addUser(user);
		return "redirect:/admins/show/users/get";
	}
	
	@GetMapping("update/user/enable")
	public String enableUser(@RequestParam("userId") int id) {
		
		User user = libraryDAO.getUserById(id);
		user.setEnabled(true);
		libraryDAO.addUser(user);
		return "redirect:/admins/show/users/get";
	}
	
	@GetMapping("update/user/update")
	public String updateUser(@RequestParam("userId") int id, Model theModel) {
		User user = libraryDAO.getUserById(id);
		List<Authority> auths = libraryDAO.getAllAuthorities();
		
		theModel.addAttribute("user",user);
		theModel.addAttribute("auths", auths);
		
		return "admin/user-form";
	}
	
	
	// delete
	@GetMapping("delete/employee")
	public String deleteEmployee(@RequestParam ("emplId") int id) {
		libraryDAO.deleteEmployee(id);
		return "redirect:/admins/show/employees/get";
	}
	
	@GetMapping("delete/user/delete")
	public String deleteUser(@RequestParam("userId") int id) {
		libraryDAO.deleteUser(id);
		
		return "redirect:/admins/show/users/get";
	}
}

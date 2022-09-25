package com.bookstore.entity;

import java.util.HashSet;
import java.util.Set;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity(name="users")
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="username")
	private String username;
	@Column(name="password")
	private String password;
	@Column(name="enabled")
	private Boolean enabled;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="user_auth",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="role_id")
			)
	private Set<Authority> roles;

	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade = CascadeType.ALL,
			mappedBy="users"
			)
	private Set<Employee> employees;
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade = CascadeType.ALL,
			mappedBy="users_client"
			)
	private Set<Client> clients;
	
	
	public User() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Authority> getRoles() {
		return roles;
	}

	public void setRoles(Set<Authority> roles) {
		this.roles = roles;
	}

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void addAuthority(Authority auth) {
		if(roles==null) roles = new HashSet<>();
		roles.add(auth);
	
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", enabled=" + enabled
				+ ", roles=" + roles + "]";
	}
	
	public void addEmployee(Employee emp) {
		if(employees==null) employees = new HashSet<>();
		employees.add(emp);
	}
	
	public void addClient(Client client) {
		if(clients==null) clients = new HashSet<>();
		clients.add(client);
	}
}

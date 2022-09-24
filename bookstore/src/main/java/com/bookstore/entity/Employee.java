package com.bookstore.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(name="employee")
@Entity(name="employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="emplid")
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="surname")
	private String surname;
	@Column(name="email")
	private String email;
	

	@ManyToMany(fetch=FetchType.LAZY,
			cascade = CascadeType.ALL)
	@JoinTable(
			name="employee_connector",
			joinColumns=@JoinColumn(name="user_id_user"),
			inverseJoinColumns = @JoinColumn(name="user_id_spring"),
			uniqueConstraints = @UniqueConstraint(columnNames = {"user_id_spring","user_id_user"})
			)
	private Set<User> users;
	
	public Employee() {
	}

	public Employee(int id, String name, String surname, String email) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", surname=" + surname + ", email=" + email + "]";
	}
	
	public void addUser(User user) {
		if(users==null) users = new HashSet<>();
		users.add(user);
	}
	
}

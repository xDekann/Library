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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Table(name="client")
@Entity(name="client")
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="clientid")
	private int id;
	@Column(name="name")
	@NotEmpty(message = "must not be empty")
	private String name;
	@Column(name="surname")
	@NotEmpty(message = "must not be empty")
	private String surname;
	@Column(name="email")
	@NotEmpty(message = "must not be empty")
	@Email(message = "provide valid email")
	private String email;
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade = CascadeType.ALL)
	@JoinTable(
			name="client_connector",
			joinColumns=@JoinColumn(name="user_id_userC"),
			inverseJoinColumns = @JoinColumn(name="user_id_springC"),
			uniqueConstraints = @UniqueConstraint(columnNames = {"user_id_springC","user_id_userC"})
			)
	private Set<User> users_client;
	
	@OneToMany(mappedBy = "client")
	private List<BookRent> clientRents;
	
	public Client() {
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

	public Set<User> getUsers_client() {
		return users_client;
	}

	public void setUsers_client(Set<User> users_client) {
		this.users_client = users_client;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", name=" + name + ", surname=" + surname + ", email=" + email + "]";
	}
	
	public void addUser(User user) {
		if(users_client==null) users_client = new HashSet<>();
		users_client.add(user);
	}

	public void addRent(BookRent rent) {
		if(clientRents==null) clientRents = new ArrayList<>();
		clientRents.add(rent);
		rent.setClient(this);
	}
}

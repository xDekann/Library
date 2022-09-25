package com.bookstore.entity;

import java.util.ArrayList;
import java.util.List;

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
import javax.validation.constraints.NotEmpty;

@Table(name="author")
@Entity(name="author")
public class Author {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="authorid")
	private int id;
	@Column(name="name")
	@NotEmpty(message = "must not be empty")
	private String name;
	@Column(name="surname")
	@NotEmpty(message = "must not be empty")
	private String surname;
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade = CascadeType.ALL)
	@JoinTable(
			name="author_connector",
			joinColumns = @JoinColumn(name="author_idC"),
			inverseJoinColumns = @JoinColumn(name="book_idC")
			)
	private List<Book> books;
	
	public Author() {
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
	
	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public void addBook(Book book) {
		if(books==null) books = new ArrayList<>();
		books.add(book);
	}
	
	@Override
	public String toString() {
		return "Author [id=" + id + ", name=" + name + ", surname=" + surname + "]";
	}
}

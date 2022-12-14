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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;


@Table(name="book")
@Entity(name="book")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="bookid")
	private int id;
	@Column(name="isbn")
	private long isbn;
	@NotEmpty(message = "must not be empty")
	@Column(name="title")
	private String title;
	@NotEmpty(message = "must not be empty")
	@Column(name="publishing_house")
	private String publishingHouse;
	@Column(name="year_of_publishment")
	@Min(value = 1900, message = "year must be 1900 or higher")
	private int yOfPublishment;
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade = CascadeType.ALL)
	@JoinTable(
			name="author_connector",
			joinColumns = @JoinColumn(name="book_idC"),
			inverseJoinColumns = @JoinColumn(name="author_idC")
			)
	private List<Author> authors;
	
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "fk_book")
	private List<BookCopy> copies;
	
	
	public Book() {
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public long getIsbn() {
		return isbn;
	}

	public void setIsbn(long isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublishingHouse() {
		return publishingHouse;
	}
	public void setPublishingHouse(String publishingHouse) {
		this.publishingHouse = publishingHouse;
	}
	public int getyOfPublishment() {
		return yOfPublishment;
	}
	public void setyOfPublishment(int yOfPublishment) {
		this.yOfPublishment = yOfPublishment;
	}

	public void addAuthor(Author author) {
		if(authors==null) authors = new ArrayList<>();
		authors.add(author);
	}
	
	public List<BookCopy> getCopies() {
		return copies;
	}

	public void setCopies(List<BookCopy> copies) {
		this.copies = copies;
	}

	public void addCopy(BookCopy theCopy) {
		if(copies==null) copies = new ArrayList<>();
		copies.add(theCopy);
	}
	
	@Override
	public String toString() {
		return "Book [id=" + id + ", isbn=" + isbn + ", title=" + title + ", publishingHouse=" + publishingHouse
				+ ", yOfPublishment=" + yOfPublishment + ", borrowTime=" + ", authors=" + authors + "]";
	}
}

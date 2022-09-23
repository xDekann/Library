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

@Table(name="book")
@Entity(name="book")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="bookid")
	private int id;
	@Column(name="isbn")
	private int isbn;
	@Column(name="title")
	private String title;
	@Column(name="publishing_house")
	private String publishingHouse;
	@Column(name="year_of_publishment")
	private int yOfPublishment;
	@Column(name="borrow_time")
	private int borrowTime;
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade = CascadeType.ALL)
	@JoinTable(
			name="author_connector",
			joinColumns = @JoinColumn(name="book_idC"),
			inverseJoinColumns = @JoinColumn(name="author_idC")
			)
	private List<Author> authors;
	
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

	public int getIsbn() {
		return isbn;
	}
	public void setIsbn(int isbn) {
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
	public int getBorrowTime() {
		return borrowTime;
	}
	public void setBorrowTime(int borrowTime) {
		this.borrowTime = borrowTime;
	}

	public void addAuthor(Author author) {
		if(authors==null) authors = new ArrayList<>();
		authors.add(author);
	}
	
	@Override
	public String toString() {
		return "Book [id=" + id + ", isbn=" + isbn + ", title=" + title + ", publishingHouse=" + publishingHouse
				+ ", yOfPublishment=" + yOfPublishment + ", borrowTime=" + borrowTime + ", authors=" + authors + "]";
	}
	
}

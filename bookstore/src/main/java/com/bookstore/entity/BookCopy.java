package com.bookstore.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name="book_copy")
@Entity(name="book_copy")
public class BookCopy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="copyid")
	private int id;
	@Column(name="isbn")
	private long isbn;
	@Column(name="title")
	private String title;
	@Column(name="status")
	private boolean status;
	@Column(name="fk_book")
	private int fkBook;
	
	//@ManyToOne(fetch=FetchType.LAZY)
	//private Book book;
	
	/*
	@ManyToMany(fetch=FetchType.LAZY,
			cascade = CascadeType.ALL,
			mappedBy="copiesBorrowed"
			)
	private List<Client> clientsWhoBorrowed;
	*/
	
	@OneToMany(mappedBy = "copy")
	private List<BookRent> copyRents;
	
	public BookCopy() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getIsbn() {
		return isbn;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setIsbn(long isbn) {
		this.isbn = isbn;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public int getFkBook() {
		return fkBook;
	}

	public void setFkBook(int fkBook) {
		this.fkBook = fkBook;
	}

	@Override
	public String toString() {
		return "BookCopy [id=" + id + ", isbn=" + isbn + ", status=" + status + ", book=" + "]";
	}
	
	/*
	public void addClient(Client client) {
		if(clientsWhoBorrowed==null) clientsWhoBorrowed = new ArrayList<>();
		clientsWhoBorrowed.add(client);
	}
	*/
	
	public void addRent(BookRent rent) {
		if(copyRents==null) copyRents = new ArrayList<>();
		copyRents.add(rent);
		rent.setCopy(this);
	}
	
}

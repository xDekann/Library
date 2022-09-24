package com.bookstore.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
	@Column(name="status")
	private boolean status;
	@Column(name="fk_book")
	private int fkBook;
	
	//@ManyToOne(fetch=FetchType.LAZY)
	//private Book book;

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
	
	
	
}

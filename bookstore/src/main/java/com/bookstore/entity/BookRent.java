package com.bookstore.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name="book_rent")
@Table(name="book_rent")
public class BookRent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="rentid")
	private int id;
	@Column(name="title")
	private String title;
	@Column(name="date_of_rent")
	private Date dateOfRent;
	@Column(name="end_date_of_rent")
	private Date endDateOfRent;
	@Column(name="penalty")
	private boolean penalty;
	
	@ManyToOne
	@JoinColumn(name="clientidR")
	private Client client;
	
	@ManyToOne
	@JoinColumn(name="copyidR")
	private BookCopy copy;

	public BookRent() {
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Date getDateOfRent() {
		return dateOfRent;
	}

	public void setDateOfRent(Date dateOfRent) {
		this.dateOfRent = dateOfRent;
	}

	public Date getEndDateOfRent() {
		return endDateOfRent;
	}

	public void setEndDateOfRent(Date endDateOfRent) {
		this.endDateOfRent = endDateOfRent;
	}

	public boolean isPenalty() {
		return penalty;
	}

	public void setPenalty(boolean penalty) {
		this.penalty = penalty;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public BookCopy getCopy() {
		return copy;
	}

	public void setCopy(BookCopy copy) {
		this.copy = copy;
	}
}

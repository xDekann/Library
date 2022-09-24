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
	@Column(name="date_of_rent")
	private Date dateOfRentDate;
	@Column(name="en_date_of_rent")
	private Date end_date_of_rent;
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

	public Date getDateOfRentDate() {
		return dateOfRentDate;
	}

	public void setDateOfRentDate(Date dateOfRentDate) {
		this.dateOfRentDate = dateOfRentDate;
	}

	public Date getEnd_date_of_rent() {
		return end_date_of_rent;
	}

	public void setEnd_date_of_rent(Date end_date_of_rent) {
		this.end_date_of_rent = end_date_of_rent;
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

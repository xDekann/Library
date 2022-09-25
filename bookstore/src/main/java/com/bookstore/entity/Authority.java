package com.bookstore.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity(name="authorities")
@Table(name="authorities")
public class Authority {
	
	@Id
	@Column(name = "idrole")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(name="authority")
	private String authorityName;
	
    @ManyToMany(fetch=FetchType.LAZY,
    		mappedBy = "roles")
    private List <User> users;
	
	public Authority() {
	}
	
	public Authority(int id, String authorityName) {
		this.id = id;
		this.authorityName = authorityName;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getAuthorityName() {
		return authorityName;
	}
	
	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}
}

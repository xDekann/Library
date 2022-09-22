package com.bookstore.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
/*
import javax.persistence.Embeddable;

@Embeddable
public class ConnectorId implements Serializable {
	
	@Column(name="user_id_spring")
	private int idSpring;
	@Column(name="user_id_user")
	private int idUser;
	@Column(name="email")
	private String emailString;
	
	public ConnectorId() {
		
	}

	public ConnectorId(int idSpring, int idUser, String emailString) {
		this.idSpring = idSpring;
		this.idUser = idUser;
		this.emailString = emailString;
	}

	public int getIdSpring() {
		return idSpring;
	}

	public void setIdSpring(int idSpring) {
		this.idSpring = idSpring;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getEmailString() {
		return emailString;
	}

	public void setEmailString(String emailString) {
		this.emailString = emailString;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getIdSpring(),getIdUser(),getEmailString());
	}

	@Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConnectorId)) return false;
        ConnectorId that = (ConnectorId) o;
        return Objects.equals(getIdSpring(), that.getIdSpring()) &&
               Objects.equals(getIdUser(), that.getIdUser()) &&
               Objects.equals(getEmailString(), that.getEmailString());
	}
	
	
	
}
*/

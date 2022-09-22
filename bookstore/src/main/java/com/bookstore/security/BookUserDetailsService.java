package com.bookstore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bookstore.dao.AdminDAO;
import com.bookstore.entity.User;

public class BookUserDetailsService implements UserDetailsService {

	@Autowired
	private AdminDAO adminDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = adminDAO.getUserByUsername(username);
		
		System.out.println("=====================" + user);
		if(user == null) {
			throw new UsernameNotFoundException("User not found - Spring Security");
		}
		
		return new BookUserDetails(user);
	}

}

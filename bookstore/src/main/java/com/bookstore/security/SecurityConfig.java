package com.bookstore.security;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@Configuration
public class SecurityConfig{
	
	/*
	@Autowired
	private DataSource securityDataSource;
	*/
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new BookUserDetailsService();
	}
	
	
	// setting mysql connection
	/*
	@Bean
	public UserDetailsManager userDetailsManager() {
		return new JdbcUserDetailsManager(securityDataSource);
	}
	*/
	
	
	// used for DB purpose password encryption
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // authorization management
	
	/*
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        //authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
    */
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	@Bean
	public SecurityFilterChain web(HttpSecurity http) throws Exception{
		return http
				.authorizeRequests(configurer->
								   configurer.antMatchers("/").permitAll()
								   			 .antMatchers("/home").hasAnyRole("ADMIN","EMPLOYEE")
								   			 .antMatchers("/employees/**").hasAnyRole("ADMIN","EMPLOYEE")
								   			 .antMatchers("/admins/**").hasRole("ADMIN"))
				.formLogin(configurer->configurer.loginPage("/auth/showLoginPage")
						 .loginProcessingUrl("/authenticateUser")
						 .permitAll()
						 .defaultSuccessUrl("/home", true))
				.logout(configurer -> configurer.permitAll())
				.exceptionHandling(configurer->configurer.accessDeniedPage("/auth/access-denied"))
				.build();
	}
}

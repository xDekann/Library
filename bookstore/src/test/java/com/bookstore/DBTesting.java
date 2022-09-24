package com.bookstore;

import static org.mockito.ArgumentMatchers.intThat;

import java.util.List;
import java.util.Set;

import javax.management.relation.Role;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.entity.Authority;
import com.bookstore.entity.Book;
import com.bookstore.entity.Employee;

@SpringBootTest
class DBTesting{
	
	@Autowired
	private EntityManager entityManager;
	
	@Test
	void seeIfEmplUserConnectionTableWorks() {
		Query theQuery = entityManager.createNativeQuery("select e.* FROM employee e inner join employee_connector c on c.user_id_user=e.emplid", Employee.class);
		System.out.println(theQuery.getSingleResult());
	}
	@Test
	void seeIfYouCanGetAllRolesAsAuthorityArray() {
		Query theQuery = entityManager.createNativeQuery("select * from authorities", Authority.class);
		List<Authority> authorities = theQuery.getResultList();
		authorities.forEach(role -> System.out.println(role.getAuthorityName()));
	}
	@Test
	void seeIfCanGetSingleAuthorityByName() {
		String name="ROLE_ADMIN";
		Query theQuery = entityManager.createQuery("from authorities a where a.authorityName=:aname ", Authority.class);
		theQuery.setParameter("aname", name);
		
		System.out.println("seeIfCanGetSingleAuthorityByName()");
		System.out.println(((Authority) theQuery.getSingleResult()).getAuthorityName());
		
	}
	@Test
	void seeIfYouCanGetAllAuthorBooks() {
		int id=1;
		Query theQuery = entityManager.createQuery("from book b inner join fetch b.authors a where a.id=: theid",Book.class);
		theQuery.setParameter("theid", id);
		List<Book> books = theQuery.getResultList();
		books.forEach(book->System.out.println(book.getId()));
	}
}

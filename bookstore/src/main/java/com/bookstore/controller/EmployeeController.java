package com.bookstore.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookstore.dao.AdminDAO;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.entity.BookCopy;
import com.bookstore.entity.BookRent;
import com.bookstore.entity.Client;
import com.bookstore.entity.User;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
	
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private PasswordEncoder encoder;
	
	// creation
	
	@GetMapping("creation/author/form")
	public String createAuthorForm(Model theModel) {
		Author author = new Author();
		theModel.addAttribute("author", author);
		return "employee/author-form";
		
	}
	@PostMapping("creation/author/creation")
	public String createAuthor(@ModelAttribute("author") Author author) {
		adminDAO.addAuthor(author);
		return "redirect:/employees/show/authors/get";
	}
	
	@GetMapping("creation/book/form") 
	public String createBookForm(Model theModel) {
		
		Book book = new Book();
		theModel.addAttribute("book",book);
		
		return "employee/book-form";
	}
	
	@PostMapping("creation/book/creation") //
	public String createBook(@RequestParam(value="nicknames") String authors, 
			@ModelAttribute("book") Book book) {
		
		//Author author = adminDAO.getAuthor(authorId);
		//author.addBook(book);
		//adminDAO.addBook(book);
		
		//authors.forEach(author->System.out.println(author));
		
		// getting all authors name+surname into separate strings
		List<String> authorsObtained = Arrays.asList(authors.split(", "));
		List<String> separateDetails = null;
		String name;
		String surname;
		Author dbAuthor;
		
		for(String author : authorsObtained) {
			// getting one author's name in one and surname in second string
			separateDetails = Arrays.asList(author.split(" "));
			name = separateDetails.get(0);
			surname = separateDetails.get(1);
			dbAuthor = adminDAO.getAuthorByDetails(name, surname);
			if(dbAuthor == null) {
				dbAuthor = new Author();
				dbAuthor.setName(name);
				dbAuthor.setSurname(surname);
				//book.addAuthor(dbAuthor);
				//adminDAO.addAuthor(dbAuthor);
			}
			book.addAuthor(dbAuthor);
			dbAuthor=null;
		}
		
		adminDAO.addBook(book);
		
		return "redirect:/employees/show/books/get";
	}
	
	@GetMapping("creation/book/creation/add/quantity")
	public String addBookQuantity(@RequestParam ("bookId") int id, Model theModel) {
		
		Book book = adminDAO.getBookById(id);
		BookCopy bookCopy = new BookCopy();
		
		bookCopy.setFkBook(id);
		bookCopy.setIsbn(book.getIsbn());
		bookCopy.setStatus(true);
		bookCopy.setTitle(book.getTitle());
		
		adminDAO.addBookCopy(bookCopy);
		//book.addCopy(bookCopy);
		
		theModel.addAttribute("books", adminDAO.getAllBooks());
		
		return "employee/pure-books";
	}
	
	@GetMapping("creation/client/form") 
	public String createClientForm(Model theModel) {
		
		Client client = new Client();
		theModel.addAttribute("client",client);
		
		return "employee/client-form";
	}
	@PostMapping("creation/client/creation")
	public String createClient(@ModelAttribute("client") Client client) {
		// add account for newly created employee
		// temporarily disabled - need configuration by admin!
		User user = new User();
		user.setEnabled(false);
		user.setUsername(client.getName()+" "+client.getSurname());
		user.setPassword(encoder.encode(client.getName()+" "+client.getSurname()));
		
		
		// link user to account and vice versa
		client.addUser(user);
		user.addClient(client);
		
		// update the link
		adminDAO.addClient(client);
		
		return "redirect:/employees/show/clients/get";
	}
	
	@GetMapping("creation/clients/rent")
	public String createRent(@RequestParam("clientId") int id,
			@RequestParam(value="titleCopy", required=false) String titleCopy,
			@RequestParam(value="phCopy", required = false) String phCopy,
			@RequestParam(value="yopCopy", required = false) String yopCopy,
			Model theModel) {
		
		// show copies of book that meets the parameters
		List<BookCopy> copies = adminDAO.getCopiesForRent(titleCopy, phCopy, yopCopy);
		
		theModel.addAttribute("copies",copies);
		theModel.addAttribute("clientId",id);
		
		return "employee/show-bcopies";
	}
	// ----------------------------------------------------------------------------
	@GetMapping("creation/clients/addrent/{copyId}/{clientId}")
	public String addRentToClient(@PathVariable("copyId") int copyId,
			@PathVariable("clientId") int clientId, RedirectAttributes redirectAttributes) {
		
		System.out.print("in double curly");
		System.out.println("client id:"+clientId);
		System.out.println("copy id:"+copyId);
		
		// final rent addition
		BookCopy copy = adminDAO.getBookCopyById(copyId);
		Client client = adminDAO.getClientById(clientId);
		copy.setStatus(false);
		BookRent rent = new BookRent();
		rent.setId(0);
		rent.setTitle(copy.getTitle());
		rent.setDateOfRent(new Date(System.currentTimeMillis()));
		rent.setPenalty(false);
		
		copy.addRent(rent);
		client.addRent(rent);
		
		adminDAO.addRent(rent);
		
		redirectAttributes.addAttribute("clientId",clientId);
		
		return "redirect:/employees/show/clients/rents";
	}
	
	@GetMapping("create/client/createpenal/{clientId}/{rentId}")
	public String addPenal(@PathVariable("rentId") int rentId,
							 @PathVariable("clientId") int clientId,
							 RedirectAttributes redirectAttributes) {
		
		BookRent rent = adminDAO.getRentById(rentId);
		rent.setPenalty(true);
		
		adminDAO.addRent(rent);
		
		redirectAttributes.addAttribute("clientId",clientId);
		return "redirect:/employees/show/clients/rents";
	}
	
	// select/show
	
	@GetMapping("show/authors/get")
	public String showAllAuthors(Model theModel) {
		theModel.addAttribute("authors", adminDAO.getAllAuthors());
		return "employee/show-authors";
	}
	
	
	@GetMapping("show/book/showall") // author list related
	public String showAllAuthorBooks(@RequestParam("authorId") int id, Model theModel) {
		
		theModel.addAttribute("books", adminDAO.getAuthorBooks(id));
		theModel.addAttribute("author", adminDAO.getAuthor(id));
		
		return "employee/author-books";
		
	}
	@GetMapping("show/books/get") // pure book show
	public String showAllBooks(Model theModel) {
		
		List<Book> allBooks = adminDAO.getAllBooks();
		
		theModel.addAttribute("books", allBooks);
		
		String bookTitleForSearch="";
		theModel.addAttribute("titleS", bookTitleForSearch);
		// state of pure-books.html span
		theModel.addAttribute("quantity", new String("0"));
		
		return "employee/pure-books";
	}
	
	@GetMapping("show/clients/get")
	public String showAllClients(Model theModel) {
		
		theModel.addAttribute("clients", adminDAO.getAllClients());
		
		return "employee/show-clients";
	}
	
	@GetMapping("show/books/get/viasearch")
	public String showCertainBooks(@RequestParam (value="titleS",required = false) String name, Model theModel) {
		theModel.addAttribute("books",adminDAO.getBooksByName(name));
		// state of pure-books.html span
		theModel.addAttribute("quantity", new String("0"));
		return "employee/pure-books";
	}
	
	@GetMapping("show/authors/get/viasearch")
	public String showCertainAuthors(@RequestParam (value="authorS", required=false) String surname, Model theModel) {
		theModel.addAttribute("authors",adminDAO.getAuthorsBySurname(surname));
		return "employee/show-authors";
	}
	
	@GetMapping("show/clients/get/viasearch")
	public String showCertainClients(@RequestParam (value="clientS", required=false) String email, Model theModel) {
		theModel.addAttribute("clients",adminDAO.getClientsByEmail(email));
		return "employee/show-clients";
	}
	@GetMapping("show/clients/rents")
	public String showClientsRents(@RequestParam("clientId") int id, Model theModel) {
		
		List<BookRent> rents = adminDAO.getClientRents(id);
		
		Collections.sort(rents, new Comparator<BookRent>(){
			@Override
			public int compare(BookRent o1, BookRent o2) {
				
				return o2.getId()-o1.getId();
			}	
		});
		
		
		theModel.addAttribute("rents", rents);
		theModel.addAttribute("clientId", id);
		
		
		return "employee/show-rents";
	}
	
	// update
	
	@GetMapping("update/client/form")
	public String updateClient(@RequestParam("clientId") int id, Model theModel) {
		
		theModel.addAttribute("client", adminDAO.getClientById(id));
		
		return "employee/client-form";
	}
	
	// delete
	
	@GetMapping("deletion/book/deletion/del/quantity")
	public String deleteBookQuantity(@RequestParam ("bookId") int id, Model theModel) {
		
		Book book = adminDAO.getBookById(id);
		BookCopy bookCopy = adminDAO.getBookCopyByBookId(book.getId());
		
		if(bookCopy!=null) {
			adminDAO.deleteBookCopy(bookCopy.getId());
			book.getCopies().remove(bookCopy);
		}
		
		theModel.addAttribute("books", adminDAO.getAllBooks());
		
		return "employee/pure-books";
	}
	
	@GetMapping("delete/client/delete")
	public String deleteClient(@RequestParam("clientId") int id) {
		adminDAO.deleteClient(id);
		
		return "redirect:/employees/show/clients/get";
	}
	@GetMapping("delete/client/cancelrent/{clientId}/{rentId}")
	public String cancelRent(@PathVariable("rentId") int rentId,
							 @PathVariable("clientId") int clientId,
							 RedirectAttributes redirectAttributes) {
		
		//System.out.println("rentId:"+rentId);
		//System.out.println("clientId:"+clientId);
		
		
		BookRent rent = adminDAO.getRentById(rentId);
		// update end date
		rent.setEndDateOfRent(new Date(System.currentTimeMillis()));
		// get difference between start-end
		LocalDate startDate = rent.getDateOfRent().toLocalDate();
		LocalDate endDate = rent.getEndDateOfRent().toLocalDate();
		// if the difference exceeds 7 days, add penalty
		long difference=ChronoUnit.DAYS.between(startDate, endDate);
		if(difference>7) {
			rent.setPenalty(true);
		}

		System.out.println(difference);
		adminDAO.getRentParentCopy(rentId).setStatus(true);
		adminDAO.addRent(rent);
		
		redirectAttributes.addAttribute("clientId",clientId);
		return "redirect:/employees/show/clients/rents";
		
	}
	
	@GetMapping("delete/client/cancelpenal/{clientId}/{rentId}")
	public String cancelPenal(@PathVariable("rentId") int rentId,
							 @PathVariable("clientId") int clientId,
							 RedirectAttributes redirectAttributes) {
		
		BookRent rent = adminDAO.getRentById(rentId);
		rent.setPenalty(false);
		
		adminDAO.addRent(rent);
		
		redirectAttributes.addAttribute("clientId",clientId);
		return "redirect:/employees/show/clients/rents";
	}
	
	// other
	@GetMapping("show/books/get/available")
	public String getBookAvailability(@RequestParam("bookId") int id, Model theModel) {
		
		
		theModel.addAttribute("books", adminDAO.getAllBooks());
		
		int quantityVal=0;
		Book checkForAvailableCopies = adminDAO.getBookById(id);
		List<BookCopy> copies = checkForAvailableCopies.getCopies();
		
		for(BookCopy copy : copies) {
			if(copy.isStatus()==true) quantityVal++;
		}
		
		// state of pure-books.html span
		if(quantityVal==0) theModel.addAttribute("quantity", new String("0"));
		else 			theModel.addAttribute("quantity", new String("1"));
		
		theModel.addAttribute("quantityVal",quantityVal);
		
		return "employee/pure-books";
		
	}
}

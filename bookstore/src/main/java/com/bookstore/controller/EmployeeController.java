package com.bookstore.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookstore.dao.LibraryDAO;
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
	private LibraryDAO libraryDAO;
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
	public String createAuthor(@Valid @ModelAttribute("author") Author author,
							   BindingResult bs) {
		if(bs.hasErrors()) return "employee/author-form";
		
		libraryDAO.addAuthor(author);
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
			@Valid @ModelAttribute("book") Book book , BindingResult bs) {
				
		if(bs.hasErrors()) return "employee/book-form";
		
		if(LocalDate.now().getYear()<book.getyOfPublishment())
			book.setyOfPublishment(LocalDate.now().getYear());
		
		// getting all authors name+surname into separate strings
		List<String> authorsObtained = Arrays.asList(authors.split(", "));
		List<String> separateDetails = null;
		String name;
		String surname;
		Author dbAuthor;
		
		for(String author : authorsObtained) {
			// getting one author's name in one and surname in second string
			separateDetails = Arrays.asList(author.split(" "));
			if(separateDetails.size()%2!=0) {
				book.setAuthors(null);
				return "employee/book-form";
			}
			name = separateDetails.get(0);
			surname = separateDetails.get(1);
			dbAuthor = libraryDAO.getAuthorByDetails(name, surname);
			if(dbAuthor == null) {
				dbAuthor = new Author();
				dbAuthor.setName(name);
				dbAuthor.setSurname(surname);
			}
			book.addAuthor(dbAuthor);
			dbAuthor=null;
		}
		libraryDAO.addBook(book);
		return "redirect:/employees/show/books/get";
	}
	
	@GetMapping("creation/book/creation/add/quantity")
	public String addBookQuantity(@RequestParam ("bookId") int id, Model theModel) {
		
		Book book = libraryDAO.getBookById(id);
		BookCopy bookCopy = new BookCopy();
		
		bookCopy.setFkBook(id);
		bookCopy.setIsbn(book.getIsbn());
		bookCopy.setStatus(true);
		bookCopy.setTitle(book.getTitle());
		
		libraryDAO.addBookCopy(bookCopy);;
		
		theModel.addAttribute("books", libraryDAO.getAllBooks());
		
		return "employee/pure-books";
	}
	
	@GetMapping("creation/client/form") 
	public String createClientForm(Model theModel) {
		
		Client client = new Client();
		theModel.addAttribute("client",client);
		
		return "employee/client-form";
	}
	@PostMapping("creation/client/creation")
	public String createClient(@Valid @ModelAttribute("client") Client client,
							   BindingResult bs) {
		
		if(bs.hasErrors()) return "employee/client-form";
		
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
		libraryDAO.addClient(client);
		
		return "redirect:/employees/show/clients/get";
	}
	
	@GetMapping("creation/clients/rent")
	public String createRent(@RequestParam("clientId") int id,
			@RequestParam(value="titleCopy", required=false) String titleCopy,
			@RequestParam(value="phCopy", required = false) String phCopy,
			@RequestParam(value="yopCopy", required = false) String yopCopy,
			Model theModel) {
		
		// show copies of book that meets the parameters
		List<BookCopy> copies = libraryDAO.getCopiesForRent(titleCopy, phCopy, yopCopy);
		
		theModel.addAttribute("copies",copies);
		theModel.addAttribute("clientId",id);
		
		return "employee/show-bcopies";
	}
	@GetMapping("creation/clients/addrent/{copyId}/{clientId}")
	public String addRentToClient(@PathVariable("copyId") int copyId,
			@PathVariable("clientId") int clientId, RedirectAttributes redirectAttributes) {
			
		// final rent addition
		BookCopy copy = libraryDAO.getBookCopyById(copyId);
		Client client = libraryDAO.getClientById(clientId);
		copy.setStatus(false);
		BookRent rent = new BookRent();
		rent.setId(0);
		rent.setTitle(copy.getTitle());
		rent.setDateOfRent(new Date(System.currentTimeMillis()));
		rent.setPenalty(false);
		
		copy.addRent(rent);
		client.addRent(rent);
		
		libraryDAO.addRent(rent);
		
		redirectAttributes.addAttribute("clientId",clientId);
		
		return "redirect:/employees/show/clients/rents";
	}
	
	@GetMapping("create/client/createpenal/{clientId}/{rentId}")
	public String addPenal(@PathVariable("rentId") int rentId,
							 @PathVariable("clientId") int clientId,
							 RedirectAttributes redirectAttributes) {
		
		BookRent rent = libraryDAO.getRentById(rentId);
		rent.setPenalty(true);
		
		libraryDAO.addRent(rent);
		
		redirectAttributes.addAttribute("clientId",clientId);
		return "redirect:/employees/show/clients/rents";
	}
	
	// select/show
	@GetMapping("show/authors/get")
	public String showAllAuthors(Model theModel) {
		theModel.addAttribute("authors", libraryDAO.getAllAuthors());
		return "employee/show-authors";
	}
	
	@GetMapping("show/book/showall") // author list related
	public String showAllAuthorBooks(@RequestParam("authorId") int id, Model theModel) {
		
		theModel.addAttribute("books", libraryDAO.getAuthorBooks(id));
		theModel.addAttribute("author", libraryDAO.getAuthor(id));
		
		return "employee/author-books";
		
	}
	@GetMapping("show/books/get") // pure book show
	public String showAllBooks(Model theModel) {
		
		List<Book> allBooks = libraryDAO.getAllBooks();
		
		theModel.addAttribute("books", allBooks);
		
		String bookTitleForSearch="";
		theModel.addAttribute("titleS", bookTitleForSearch);
		
		// state of pure-books.html span
		theModel.addAttribute("quantity", new String("0"));
		
		return "employee/pure-books";
	}
	
	@GetMapping("show/clients/get")
	public String showAllClients(Model theModel) {
		
		theModel.addAttribute("clients", libraryDAO.getAllClients());
		
		return "employee/show-clients";
	}
	
	@GetMapping("show/books/get/viasearch")
	public String showCertainBooks(@RequestParam (value="titleS",required = false) String name, Model theModel) {
		theModel.addAttribute("books",libraryDAO.getBooksByName(name));
		// state of pure-books.html span
		theModel.addAttribute("quantity", new String("0"));
		return "employee/pure-books";
	}
	
	@GetMapping("show/authors/get/viasearch")
	public String showCertainAuthors(@RequestParam (value="authorS", required=false) String surname, Model theModel) {
		theModel.addAttribute("authors",libraryDAO.getAuthorsBySurname(surname));
		return "employee/show-authors";
	}
	
	@GetMapping("show/clients/get/viasearch")
	public String showCertainClients(@RequestParam (value="clientS", required=false) String email, Model theModel) {
		theModel.addAttribute("clients",libraryDAO.getClientsByEmail(email));
		return "employee/show-clients";
	}
	@GetMapping("show/clients/rents")
	public String showClientsRents(@RequestParam("clientId") int id, Model theModel) {
		
		List<BookRent> rents = libraryDAO.getClientRents(id);
		
		// sort client rents based on id (fresh ones on top)
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
		
		theModel.addAttribute("client", libraryDAO.getClientById(id));
		
		return "employee/client-form";
	}
	
	// delete
	@GetMapping("deletion/book/deletion/del/quantity")
	public String deleteBookQuantity(@RequestParam ("bookId") int id, Model theModel) {
		
		Book book = libraryDAO.getBookById(id);
		BookCopy bookCopy = libraryDAO.getBookCopyByBookId(book.getId());
		
		if(bookCopy!=null) {
			libraryDAO.deleteBookCopy(bookCopy.getId());
			book.getCopies().remove(bookCopy);
		}
		
		theModel.addAttribute("books", libraryDAO.getAllBooks());
		
		return "employee/pure-books";
	}
	
	@GetMapping("delete/client/delete")
	public String deleteClient(@RequestParam("clientId") int id) {
		libraryDAO.deleteClient(id);
		
		return "redirect:/employees/show/clients/get";
	}
	@GetMapping("delete/client/cancelrent/{clientId}/{rentId}")
	public String cancelRent(@PathVariable("rentId") int rentId,
							 @PathVariable("clientId") int clientId,
							 RedirectAttributes redirectAttributes) {	
		
		BookRent rent = libraryDAO.getRentById(rentId);
		// update end date
		rent.setEndDateOfRent(new Date(System.currentTimeMillis()));
		// get difference between start-end
		LocalDate startDate = rent.getDateOfRent().toLocalDate();
		LocalDate endDate = rent.getEndDateOfRent().toLocalDate();
		// if the difference exceeds or equals 7 days, add penalty
		long difference=ChronoUnit.DAYS.between(startDate, endDate);
		if(difference>=7) {
			rent.setPenalty(true);
		}

		libraryDAO.getRentParentCopy(rentId).setStatus(true);
		libraryDAO.addRent(rent);
		
		redirectAttributes.addAttribute("clientId",clientId);
		return "redirect:/employees/show/clients/rents";
		
	}
	
	@GetMapping("delete/client/cancelpenal/{clientId}/{rentId}")
	public String cancelPenal(@PathVariable("rentId") int rentId,
							 @PathVariable("clientId") int clientId,
							 RedirectAttributes redirectAttributes) {
		
		BookRent rent = libraryDAO.getRentById(rentId);
		rent.setPenalty(false);
		
		libraryDAO.addRent(rent);
		
		redirectAttributes.addAttribute("clientId",clientId);
		return "redirect:/employees/show/clients/rents";
	}
	
	// other
	@GetMapping("show/books/get/available")
	public String getBookAvailability(@RequestParam("bookId") int id, Model theModel) {
		
		
		theModel.addAttribute("books", libraryDAO.getAllBooks());
		
		int quantityVal=0;
		Book checkForAvailableCopies = libraryDAO.getBookById(id);
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

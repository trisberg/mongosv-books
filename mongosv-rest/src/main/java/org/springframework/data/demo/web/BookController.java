package org.springframework.data.demo.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.demo.domain.Author;
import org.springframework.data.demo.domain.Book;
import org.springframework.data.demo.hateoas.AuthorResource;
import org.springframework.data.demo.hateoas.BookResource;
import org.springframework.data.demo.repository.AuthorRepository;
import org.springframework.data.demo.repository.BookRepository;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handles book requests for the application.
 */
@Controller
@RequestMapping(value = "books")
public class BookController {
	
	private BookRepository bookRepository;

	private AuthorRepository authorRepository;

	@Autowired
	public void setBookRepository(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}
	
	@Autowired
	public void setAuthorRepository(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<BookResource> all() {
		List<BookResource> bookResources = new ArrayList<BookResource>();
		for (Book book : bookRepository.findAll()) {
			bookResources.add(assembleResource(book));
		}
		return bookResources;
	}
	
	@RequestMapping(method = RequestMethod.GET, params = "category")
	@ResponseBody
	public List<BookResource> findForCategory(@RequestParam String category) {
		List<BookResource> bookResources = new ArrayList<BookResource>();
		List<Book> books;
		if (category != null) {
			books = bookRepository.findByCategoriesIn(new String[] {category});
		} else {
			books = bookRepository.findAll();
		}
		for (Book book : books) {
			bookResources.add(assembleResource(book));
		}
		return bookResources;
	}
	
	@RequestMapping(value="/{isbn}", method = RequestMethod.GET)
	@ResponseBody
	public ResourceSupport findBook(@PathVariable String isbn, 
			HttpServletResponse httpServletResponse) {
		Book book = bookRepository.findOne(isbn);
		if (book == null) {
			httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
			ResourceSupport info = new ResourceSupport();
			info.add(linkTo(BookController.class).withRel("books"));
			return info;
		}
		return assembleResource(book);
	}

	@RequestMapping(value="/{isbn}", method = RequestMethod.PUT, headers = {"content-type=application/json"})
	@ResponseBody
	public void addBook(@RequestBody BookResource resource, @PathVariable String isbn, 
			HttpServletResponse httpServletResponse) {
		Book book = new Book();
		book.setIsbn(isbn);
		book.setTitle(resource.getTitle());
		book.setPublished(resource.getPublished());
		book.setPrice(resource.getPrice());
		book.setCategories(resource.getCategories());
		boolean exists = false;
		if (bookRepository.findOne(isbn) != null) {
			exists = true;
		}
		bookRepository.save(book);
		BookResource responseResource = assembleResource(book);
		httpServletResponse.setHeader("Location", responseResource.getId().getHref());
		if (exists) {
			httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
		} else {
			httpServletResponse.setStatus(HttpStatus.CREATED.value());
		}
	}

	@RequestMapping(value="/{isbn}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public void deleteBook(@PathVariable String isbn) {
		bookRepository.delete(isbn);
	}

	@RequestMapping(value="/{isbn}/authors", method = RequestMethod.GET)
	@ResponseBody
	public List<AuthorResource> findBookAuthors(@PathVariable String isbn, 
			HttpServletResponse httpServletResponse) {
		Book book = bookRepository.findOne(isbn);
		List<AuthorResource> authors = new ArrayList<AuthorResource>();
		if (book == null) {
			httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());			
			return authors;
		}
		for (Author author : book.getAuthors()) {
			authors.add(AuthorController.assembleResource(author));
		}
		return authors;
	}

	@RequestMapping(value="/{isbn}/authors/{id}", method = RequestMethod.POST)
	@ResponseBody
	public void addBookAuthor(@PathVariable String isbn, @PathVariable String id, 
			HttpServletResponse httpServletResponse) {
		Book book = bookRepository.findOne(isbn);
		if (book == null) {
			httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());			
		}
		for (Author author : book.getAuthors()) {
			if (author.getId().equals(id)) {
				httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());			
				return;
			}			
		}
		Author author = authorRepository.findOne(id);
		book.addAuthor(author);
		bookRepository.save(book);
		httpServletResponse.setStatus(HttpStatus.CREATED.value());			
	}

	@RequestMapping(value="/{isbn}/authors/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public void removeBookAuthor(@PathVariable String isbn, @PathVariable String id, 
			HttpServletResponse httpServletResponse) {
		Book book = bookRepository.findOne(isbn);
		if (book == null) {
			httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());			
		}
		for (Author author : book.getAuthors()) {
			if (author.getId().equals(id)) {
				book.removeAuthor(author);
				break;
			}			
		}
		bookRepository.save(book);
	}

	public static BookResource assembleResource(Book book) {
		BookResource resource = new BookResource(book.getTitle(), book.getIsbn(), book.getPublished(), 
				book.getPrice(),book.getCategories());
		Link self = linkTo(BookController.class).slash(book.getIsbn()).withSelfRel();
		resource.add(self);
		Link authorLink = linkTo(BookController.class).slash(book.getIsbn()).slash("authors").withRel("authors");
		resource.add(authorLink);
		return resource;
	}

}

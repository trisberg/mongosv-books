package org.springframework.data.demo.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.demo.domain.Author;
import org.springframework.data.demo.domain.Book;
import org.springframework.data.demo.hateoas.AuthorResource;
import org.springframework.data.demo.hateoas.BookResource;
import org.springframework.data.demo.repository.AuthorRepository;
import org.springframework.data.demo.repository.BookRepository;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;

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
	public @ResponseBody List<BookResource> all() {
		List<BookResource> bookResources = new ArrayList<BookResource>();
		for (Book book : bookRepository.findAll()) {
			bookResources.add(assembleResource(book));
		}
		return bookResources;
	}
	
	@RequestMapping(method = RequestMethod.GET, params = "category")
	public @ResponseBody List<BookResource> findForCategory(@RequestParam String category) {
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
	public ResponseEntity<ResourceSupport> findBook(@PathVariable String isbn) {
		Book book = bookRepository.findOne(isbn);
		if (book == null) {
			ResourceSupport info = new ResourceSupport();
			info.add(linkTo(BookController.class).withRel("books"));
			return new ResponseEntity<ResourceSupport>(info, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResourceSupport>(assembleResource(book), HttpStatus.OK);
	}

	@RequestMapping(value="/{isbn}", method = RequestMethod.PUT, headers = {"content-type=application/json"})
	public ResponseEntity<Object> addBook(@RequestBody BookResource resource, @PathVariable String isbn) {
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
		HttpHeaders headers = new HttpHeaders();
		headers.put("Location", Arrays.asList(responseResource.getId().getHref()));
		if (exists) {
			return new ResponseEntity<Object>(null, headers, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Object>(null, headers, HttpStatus.CREATED);
		}
	}

	@RequestMapping(value="/{isbn}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public @ResponseBody void deleteBook(@PathVariable String isbn) {
		bookRepository.delete(isbn);
	}

	@RequestMapping(value="/{isbn}/authors", method = RequestMethod.GET)
	public ResponseEntity<List<AuthorResource>> findBookAuthors(@PathVariable String isbn) {
		Book book = bookRepository.findOne(isbn);
		List<AuthorResource> authors = new ArrayList<AuthorResource>();
		if (book == null) {
			return new ResponseEntity<List<AuthorResource>>(authors, HttpStatus.NOT_FOUND);
		}
		for (Author author : book.getAuthors()) {
			AuthorResource resource = AuthorController.assembleResource(author);
			Link bookLink = linkTo(BookController.class).slash(isbn).slash("authors").slash(author.getId()).withRel("bookAuthor");
			resource.add(bookLink);
			authors.add(resource);
		}
		return new ResponseEntity<List<AuthorResource>>(authors, HttpStatus.OK);
	}

	@RequestMapping(value="/{isbn}/authors", method = RequestMethod.POST, headers = "content-type=text/uri-list")
	public ResponseEntity<Object> addBookAuthor(@PathVariable String isbn, @RequestBody String body) {
		Book book = bookRepository.findOne(isbn);
		if (book == null) {
			return new ResponseEntity<Object>("", HttpStatus.BAD_REQUEST);
		}
		String id;
		id = matchAuthorIdFromLink(body);
		if (id == null) {
			return new ResponseEntity<Object>("", HttpStatus.BAD_REQUEST);
		}
		URI bookLink = linkTo(BookController.class).slash(isbn).slash("authors").slash(id).toUri();
		for (Author author : book.getAuthors()) {
			if (author.getId().equals(id)) {
				HttpHeaders headers = new HttpHeaders();
				headers.put("Location", Arrays.asList(bookLink.toString()));
				return new ResponseEntity<Object>(headers, HttpStatus.NO_CONTENT);
			}			
		}
		Author author = authorRepository.findOne(id);
		if (author == null) {
			return new ResponseEntity<Object>("", HttpStatus.NOT_FOUND);
		}
		book.addAuthor(author);
		bookRepository.save(book);
		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<Object>(headers, HttpStatus.CREATED);
	}

	public String matchAuthorIdFromLink(String uri) {
		UriTemplate ut = new UriTemplate(ControllerLinkBuilder.linkTo(AuthorController.class) + "/{id}");
		Map<String, String> ids = ut.match(uri);
		if (ids.get("id") == null) {
			return null;
		}
		String id = ids.get("id");
		return id;
	}

	@RequestMapping(value="/{isbn}/authors/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> removeBookAuthor(@PathVariable String isbn, @PathVariable String id) {
		Book book = bookRepository.findOne(isbn);
		if (book == null) {
			return new ResponseEntity<Object>("", HttpStatus.BAD_REQUEST);
		}
		for (Author author : book.getAuthors()) {
			if (author.getId().equals(id)) {
				book.removeAuthor(author);
				bookRepository.save(book);
				break;
			} else {
			}
		}
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
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

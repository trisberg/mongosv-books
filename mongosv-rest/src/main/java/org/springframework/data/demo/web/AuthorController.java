package org.springframework.data.demo.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.demo.domain.Author;
import org.springframework.data.demo.domain.Book;
import org.springframework.data.demo.hateoas.AuthorResource;
import org.springframework.data.demo.hateoas.BookResource;
import org.springframework.data.demo.repository.AuthorRepository;
import org.springframework.data.demo.repository.BookRepository;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handles author requests for the application.
 */
@Controller
@RequestMapping(value = "authors")
public class AuthorController {
	
	private AuthorRepository authorRepository;

	private BookRepository bookRepository;

	@Autowired
	public void setAuthorRepository(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}

	@Autowired
	public void setBookRepository(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<AuthorResource> all() {
		List<AuthorResource> resources = new ArrayList<AuthorResource>();
		for (Author author : authorRepository.findAll()) {
			resources.add(assembleResource(author));
		}
		return resources;
	}

	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<ResourceSupport> findAuthor(@PathVariable String id) {
		Author author = authorRepository.findOne(id);
		if (author == null) {
			ResourceSupport info = new ResourceSupport();
			info.add(linkTo(AuthorController.class).withRel("authors"));
			return new ResponseEntity<ResourceSupport>(info, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResourceSupport>(assembleResource(author), HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.PUT, headers = {"content-type=application/json"})
	public ResponseEntity<Object> putAuthor(@PathVariable String id, @RequestBody AuthorResource resource) {
		Author author = new Author();
		author.setId(id);
		author.setName(resource.getName());
		boolean exists = false;
		if (authorRepository.findOne(id) != null) {
			exists = true;
		}
		authorRepository.save(author);
		AuthorResource responseResource = assembleResource(author);
		HttpHeaders headers = new HttpHeaders();
		headers.put("Location", Arrays.asList(responseResource.getId().getHref()));
		if (exists) {
			return new ResponseEntity<Object>(null, headers, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Object>(null, headers, HttpStatus.CREATED);
		}
	}

	@RequestMapping(method = RequestMethod.POST, headers = {"content-type=application/json"})
	public ResponseEntity<AuthorResource> addAuthor(@RequestBody AuthorResource resource) {
		Author author = new Author();
		author.setName(resource.getName());
		authorRepository.save(author);
		AuthorResource responseResource = assembleResource(author);
		HttpHeaders headers = new HttpHeaders();
		headers.put("Location", Arrays.asList(responseResource.getId().getHref()));
		return new ResponseEntity<AuthorResource>(responseResource, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteBook(@PathVariable String id) {
		authorRepository.delete(id);
	}

	@RequestMapping(value="/{id}/books", method = RequestMethod.GET)
	public ResponseEntity<List<BookResource>> findBooksByAuthor(@PathVariable String id) {
		Author author = authorRepository.findOne(id);
		List<BookResource> books = new ArrayList<BookResource>();
		if (author == null) {
			return new ResponseEntity<List<BookResource>>(books, HttpStatus.NOT_FOUND);
		}
		for (Book book : bookRepository.findByAuthors(author)) {
			books.add(BookController.assembleResource(book));
		}
		return new ResponseEntity<List<BookResource>>(books, HttpStatus.OK);
	}
	
	public static AuthorResource assembleResource(Author author) {
		AuthorResource resource = new AuthorResource(author.getName());
		Link self = linkTo(AuthorController.class).slash(author.getId()).withSelfRel();
		resource.add(self);
		Link bookLink = linkTo(AuthorController.class).slash(author.getId()).slash("books").withRel("books");
		resource.add(bookLink);
		return resource;		
	}

}

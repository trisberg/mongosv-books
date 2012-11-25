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
	@ResponseBody
	public ResourceSupport findAuthor(@PathVariable String id, 
			HttpServletResponse httpServletResponse) {
		Author author = authorRepository.findOne(id);
		if (author == null) {
			httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
			ResourceSupport info = new ResourceSupport();
			info.add(linkTo(AuthorController.class).withRel("authors"));
			return info;
		}
		return assembleResource(author);
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.PUT, headers = {"content-type=application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public void putAuthor(@PathVariable String id, @RequestBody AuthorResource resource, 
			HttpServletResponse httpServletResponse) {
		Author author = new Author();
		author.setId(id);
		author.setName(resource.getName());
		boolean exists = false;
		if (authorRepository.findOne(id) != null) {
			exists = true;
		}
		authorRepository.save(author);
		AuthorResource responseResource = assembleResource(author);
		httpServletResponse.setHeader("Location", responseResource.getId().getHref());
		if (exists) {
			httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
		} else {
			httpServletResponse.setStatus(HttpStatus.CREATED.value());
		}
	}

	@RequestMapping(method = RequestMethod.POST, headers = {"content-type=application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public AuthorResource addAuthor(@RequestBody AuthorResource resource, 
			HttpServletResponse httpServletResponse) {
		Author author = new Author();
		author.setName(resource.getName());
		authorRepository.save(author);
		AuthorResource responseResource = assembleResource(author);
		httpServletResponse.setHeader("Location", responseResource.getId().getHref());
		return responseResource;
	}

	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public void deleteBook(@PathVariable String id) {
		authorRepository.delete(id);
	}

	@RequestMapping(value="/{id}/books", method = RequestMethod.GET)
	@ResponseBody
	public List<BookResource> findBooksByAuthor(@PathVariable String id, 
			HttpServletResponse httpServletResponse) {
		Author author = authorRepository.findOne(id);
		List<BookResource> books = new ArrayList<BookResource>();
		if (author == null) {
			httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
			return books;
		}
		for (Book book : bookRepository.findByAuthors(author)) {
			books.add(BookController.assembleResource(book));
		}
		return books;
	}
	
	public static AuthorResource assembleResource(Author author) {
		AuthorResource resource = new AuthorResource(author.getName());
		Link self = linkTo(AuthorController.class).slash(author.getId()).withSelfRel();
		resource.add(self);
		Link bookLink = linkTo(BookController.class).slash(author.getId()).slash("books").withRel("books");
		resource.add(bookLink);
		return resource;		
	}

}

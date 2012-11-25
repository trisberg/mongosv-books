package org.springframework.data.demo.hateoas;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import org.springframework.hateoas.ResourceSupport;

public class BookResource extends ResourceSupport {
	
	private String title;

	private String isbn;

	private Date published;
	
	private BigDecimal price;
	
	private Set<String> categories;

	public BookResource() {
	}
	
	public BookResource(String title, String isbn, Date published, BigDecimal price, Set<String> categories) {
		this.title = title;
		this.isbn = isbn;
		this.published = published;
		this.price = price;
		this.categories = categories;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public Date getPublished() {
		return published;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Set<String> getCategories() {
		return categories;
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	public String toString() {
		return "[" + this.isbn + "] " + this.title + " " + super.toString();
	}

}

package org.springframework.data.demo.hateoas;

import org.springframework.hateoas.ResourceSupport;

public class AuthorResource extends ResourceSupport {
	
	private String name;

	public AuthorResource() {
	}

	public AuthorResource(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

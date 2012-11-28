package org.springframework.data.demo.test;

import java.net.URI;

import org.springframework.data.demo.domain.Book;
import org.springframework.data.mongodb.core.mapping.Field;

public class TestBook extends Book {
	
	@Field("amazon_uri")
	URI uri;

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	@Override
	public String toString() {
		return "Test :: " + super.toString() + " " + uri;
	}

}

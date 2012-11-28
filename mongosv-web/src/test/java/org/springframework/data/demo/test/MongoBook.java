package org.springframework.data.demo.test;

import org.springframework.data.demo.domain.Book;

public class MongoBook extends Book {

	@Override
	public String toString() {
		return "MongoBook :: " + super.toString();
	}

}

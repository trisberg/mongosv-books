package org.springframework.data.demo.test;

import org.springframework.data.demo.domain.Book;

public class JavaBook extends Book {

	@Override
	public String toString() {
		return "JavaBook :: " + super.toString();
	}

}

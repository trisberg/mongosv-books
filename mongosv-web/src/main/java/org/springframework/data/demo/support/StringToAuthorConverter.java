package org.springframework.data.demo.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.demo.domain.Author;
import org.springframework.data.demo.repository.DbHelper;

public class StringToAuthorConverter implements Converter<String, Author> {
	
	@Autowired
	DbHelper dbHelper;

	@Override
	public Author convert(String s) {
		return dbHelper.findAuthorById(s);
	}

}

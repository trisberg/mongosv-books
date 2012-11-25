package org.springframework.data.demo.repository;

import java.util.List;

import org.springframework.data.demo.domain.Author;
import org.springframework.data.repository.Repository;

public interface AuthorRepository extends Repository<Author, String> {
	
	List<Author> findAll();

	Author findOne(String id);

	Author findByName(String name);
	
	Author save(Author author);
	
	void delete(String id);
}

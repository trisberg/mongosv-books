package org.springframework.data.demo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.demo.domain.Author;
import org.springframework.data.demo.domain.Book;
import org.springframework.data.repository.Repository;

public interface BookRepository extends Repository<Book, String> {
	
	Book save(Book book);
	
	Book findOne(String isbn);
	
	void delete(String isbn);
	
	List<Book> findAll(); 

	List<Book> findByAuthors(Author author); 

	List<Book> findByPublishedGreaterThan(Date date);
	
	List<Book> findByCategoriesIn(String[] categories); 

	List<Book> findByPublishedGreaterThanAndCategoriesIn(Date date, String[] categories); 

}

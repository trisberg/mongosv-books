package org.springframework.data.demo.repository;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.demo.domain.Book;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class BookRepositoryTest extends AbstractRepositoryTest {

    @Test
    public void addBook() {
    	Book b1 = newBook();
    	bookRepository.save(b1);
    	assertEquals(1, bookRepository.findAll().size());
    }

    @Test
    public void findBook() {
    	Book b1 = newBook();
    	bookRepository.save(b1);
    	assertEquals(1, bookRepository.findAll().size());
    	assertEquals(b1, bookRepository.findOne(b1.getIsbn()));
    }

    @Test
    public void deleteBook() {
    	Book b1 = newBook();
    	bookRepository.save(b1);
    	assertEquals(1, bookRepository.findAll().size());
    	bookRepository.delete(b1.getIsbn());
    	assertEquals(0, bookRepository.findAll().size());
    }

	private Book newBook() {
		Book b1 = new Book();
    	b1.setIsbn("0987654321");
    	b1.setTitle("Test");
    	b1.setPrice(new BigDecimal(22.99));
		return b1;
	}
}

package org.springframework.data.demo.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.demo.domain.Author;
import org.springframework.data.demo.domain.Book;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class RepositoryIntegrationTest extends AbstractRepositoryTest {

    @Test
    public void findBooksByAuthor() {
    	Author a1 = new Author();
    	a1.setName("Mark");
    	authorRepository.save(a1);
    	Author a2 = new Author();
    	a2.setName("Oliver");
    	authorRepository.save(a2);
    	Author a3 = new Author();
    	a3.setName("Thomas");
    	authorRepository.save(a3);
    	
    	Book b1 = new Book();
    	b1.setIsbn("0987654321");
    	b1.setTitle("Test");
    	b1.addAuthor(a1);
    	b1.addAuthor(a2);
    	b1.addAuthor(a3);
    	bookRepository.save(b1);

    	Book b2 = new Book();
    	b2.setIsbn("1234567890");
    	b2.setTitle("Spring Framework");
    	b2.addAuthor(a1);
    	bookRepository.save(b2);

    	List<Book> a1Books = bookRepository.findByAuthors(a1);
    	assertEquals(2, a1Books.size());
    	List<Book> a2Books = bookRepository.findByAuthors(a2);
    	assertEquals(1, a2Books.size());
    }
}

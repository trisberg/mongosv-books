package org.springframework.data.demo.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.demo.domain.Author;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class AuthorRepositoryTest extends AbstractRepositoryTest {

    @Test
    public void addAuthor() {
    	Author a1 = newAuthor();
    	authorRepository.save(a1);
    	assertEquals(1, authorRepository.findAll().size());
    }

    @Test
    public void addAuthorWithId() {
    	Author a1 = newAuthor();
    	ObjectId id = new ObjectId();
    	a1.setId(id.toString());
    	authorRepository.save(a1);
    	List<Author> authors = authorRepository.findAll(); 
    	assertEquals(1, authors.size());
    	assertEquals(id.toString(), authors.get(0).getId());
    }

    @Test
    public void findAuthor() {
    	Author a1 = newAuthor();
    	authorRepository.save(a1);
    	assertEquals(1, authorRepository.findAll().size());
    	assertEquals(a1, authorRepository.findOne(a1.getId()));
    }

    @Test
    public void deleteAuthor() {
    	Author a1 = newAuthor();
    	authorRepository.save(a1);
    	assertEquals(1, authorRepository.findAll().size());
    	authorRepository.delete(a1.getId());
    	assertEquals(0, authorRepository.findAll().size());
    }

	private Author newAuthor() {
		Author a1 = new Author();
    	a1.setName("Mark");
		return a1;
	}
}

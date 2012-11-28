package org.springframework.data.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.demo.domain.Book;
import org.springframework.data.demo.test.JavaBook;
import org.springframework.data.demo.test.MongoBook;
import org.springframework.data.demo.test.TestBook;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-context.xml")
public class MongoTemplateTest {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Test
	public void testSubClasses() {
		JavaBook jb = new JavaBook();
		jb.setTitle("Spring in Action");
		jb.setIsbn("0987609876");
		MongoBook mb = new MongoBook();
		mb.setTitle("Mongo in Action");
		mb.setIsbn("1234512345");
		mongoTemplate.save(jb, "test");
		mongoTemplate.save(mb, "test");
		
		Query q = new Query(Criteria.where("isbn").in("1234512345", "0987609876"));
		
		List<Book> lb = mongoTemplate.find(q, Book.class, "test");
		
		assertEquals(2, lb.size());
		for (Book b : lb) {
			if (b.getIsbn().equals(jb.getIsbn())) {
				assertEquals(JavaBook.class, b.getClass());
			} else {
				assertEquals(MongoBook.class, b.getClass());
			}
		}
	}

	@Test
	public void testCustomConverters() {
		
		final TestBook b = new TestBook();
		b.setIsbn("1449323952");
		b.setPrice(new BigDecimal("29.99"));
		b.setUri(URI.create("http://www.amazon.com/Spring-Data-Mark-Pollack/dp/1449323952"));
		
		mongoTemplate.save(b);
		
		TestBook b2 = mongoTemplate.findById("1449323952", TestBook.class);
		
		assertEquals(b.getUri(), b2.getUri());
				
		mongoTemplate.execute(TestBook.class, 
				new CollectionCallback<Object>() {
					@SuppressWarnings("rawtypes")
					@Override
					public Object doInCollection(DBCollection collection)
							throws MongoException, DataAccessException {
						Query q = new Query(Criteria.where("_id").is("1449323952"));
						DBObject result = collection.findOne(q.getQueryObject());
						assertNotNull(result.get("amazon_uri"));
						assertTrue(result.get("amazon_uri") instanceof Map);
						assertEquals(b.getUri().toString(), ((Map)result.get("amazon_uri")).get("URI"));
						return null;
					}			
				});
	}

}

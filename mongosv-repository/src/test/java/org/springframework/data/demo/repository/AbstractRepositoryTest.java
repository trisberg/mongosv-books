package org.springframework.data.demo.repository;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.demo.config.RepositoryConfig;
import org.springframework.data.demo.domain.Author;
import org.springframework.data.demo.domain.Book;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.mongodb.Mongo;

@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class AbstractRepositoryTest {

	@Configuration
	@ComponentScan(basePackageClasses = RepositoryConfig.class)
	static class ContextConfiguration extends AbstractMongoConfiguration {

		@Override
		protected String getDatabaseName() {
			return "test";
		}

		@Override
		@Bean
		public Mongo mongo() throws Exception {
			return new Mongo();
		}
    }

    protected BookRepository bookRepository;
 
    protected AuthorRepository authorRepository;
    
    protected MongoTemplate mongoTemplate;


    @Autowired
	public void setBookRepository(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

    @Autowired
	public void setAuthorRepository(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}

    @Autowired
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Before
    public void setUp() {
    	if (mongoTemplate.collectionExists(Book.class)) {
    		mongoTemplate.dropCollection(Book.class);
    	}
    	if (mongoTemplate.collectionExists(Author.class)) {
    		mongoTemplate.dropCollection(Author.class);
    	}
    }
    
}

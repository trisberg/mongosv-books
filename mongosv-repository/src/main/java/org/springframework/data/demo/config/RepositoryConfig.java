package org.springframework.data.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.demo.repository.BookRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = BookRepository.class)
public class RepositoryConfig {
}

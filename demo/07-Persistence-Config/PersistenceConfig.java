package com.springdeveloper.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;

@Configuration
@EnableMongoRepositories(basePackageClasses = DeveloperRepository.class)
public class PersistenceConfig extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return "demo";
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		return new Mongo();
	}
}

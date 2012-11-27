package org.springframework.data.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;

@Configuration
@Profile("default")
public class DefaultMongoConfig extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return "db";
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		return new Mongo();
	}

}

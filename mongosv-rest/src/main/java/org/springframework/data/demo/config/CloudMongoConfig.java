package org.springframework.data.demo.config;

import org.cloudfoundry.runtime.service.document.CloudMongoDbFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile("cloud")
public class CloudMongoConfig {

	@Bean
	CloudMongoDbFactoryBean mongoDbFactory() throws Exception {
		CloudMongoDbFactoryBean factoryBean = new CloudMongoDbFactoryBean();
		return factoryBean;
	}

	@Bean
	public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) throws Exception {
		return new MongoTemplate(mongoDbFactory);
	}
}

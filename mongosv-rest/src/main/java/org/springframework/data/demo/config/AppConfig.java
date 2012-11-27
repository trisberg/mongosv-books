package org.springframework.data.demo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackageClasses = { RepositoryConfig.class })
@Import({ DefaultMongoConfig.class, CloudMongoConfig.class })
public class AppConfig {
}

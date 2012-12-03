package com.springdeveloper.demo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeveloperRepository extends MongoRepository<Developer, String> {

}

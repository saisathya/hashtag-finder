package com.hashtag_finder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication

public class HashtagFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(HashtagFinderApplication.class, args);
	}

}
package org.springframework.data.demo.convert;

import java.net.URI;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Component
public class TestWriteConverter 
		implements Converter<URI, DBObject> {

	public DBObject convert(URI source) {
		DBObject dbo = new BasicDBObject();
		dbo.put("URI", source.toString());
		System.out.println("***[WRITE]: " + dbo);
		return dbo;
	}

}
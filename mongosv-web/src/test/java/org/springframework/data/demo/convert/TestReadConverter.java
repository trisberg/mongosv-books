package org.springframework.data.demo.convert;

import java.net.URI;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mongodb.DBObject;

@Component
public class TestReadConverter  implements Converter<DBObject, URI> {

	public URI convert(DBObject source) {
		System.out.println("***[READ]: " + source);
		URI r = URI.create(((String) source.get("URI")));
		return r;
	}
}
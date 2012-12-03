package com.springdeveloper.demo;

import java.util.ArrayList;
import java.util.List;

public class Developer {
	
	private String name;
	
	private List<String> languages = new ArrayList<String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	public void addLanguage(String language) {
		this.languages.add(language);
	}

	@Override
	public String toString() {
		return "Developer [name=" + name + ", languages=" + languages + "]";
	}
	
}

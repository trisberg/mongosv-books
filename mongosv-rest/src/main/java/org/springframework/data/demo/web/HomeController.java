package org.springframework.data.demo.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles info requests for the application.
 */
@Controller
@RequestMapping(value = "/")
public class HomeController {
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResourceSupport info() {
		ResourceSupport info = new ResourceSupport();
		info.add(linkTo(AuthorController.class).withRel("authors"));
		info.add(linkTo(BookController.class).withRel("books"));
		return info;
	}
}

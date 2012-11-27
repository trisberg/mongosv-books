package org.springframework.data.demo.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = { AppServletConfig.class })
public class AppServletConfig {
		
}

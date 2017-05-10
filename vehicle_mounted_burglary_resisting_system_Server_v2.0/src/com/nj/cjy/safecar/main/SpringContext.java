package com.nj.cjy.safecar.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContext {

	private static ApplicationContext springContext = new ClassPathXmlApplicationContext("applicationContext.xml");

	public static ApplicationContext getSpringContext() {
		return springContext;
	}

	public static void setSpringContext(ApplicationContext springContext) {
		SpringContext.springContext = springContext;
	}
	
	
}

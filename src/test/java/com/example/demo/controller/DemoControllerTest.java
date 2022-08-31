package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = DemoController.class)
@TestPropertySource(properties = {"backbase.my_secret=mysecret"})
class DemoControllerTest {
	
	@Autowired
	private DemoController demoController;

	@Test
	void testGetecret() {
		String response = demoController.getSecret();
		assertEquals("Your secret is: mysecret", response);
	}
	
	@Test
	void testGreeting() {
		String response = demoController.greeting("Backbase");
		assertEquals("Hello Backbase", response);
	}

}

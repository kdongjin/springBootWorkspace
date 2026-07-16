package com.zeus;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootFileUploadApplicationTests {

	@Test
	void contextLoads() {
		for (int i = 0; i < 10; i++) {
			UUID uid = UUID.randomUUID();
			String createdFileName = uid.toString() + "_";
			System.out.println(createdFileName);
		}
	}

}

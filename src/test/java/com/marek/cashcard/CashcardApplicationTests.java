package com.marek.cashcard;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) /*start SB app and make it available to test */
class CashcardApplicationTests {

	@Autowired /*DI notation used for tests, this is injection of test helper */
	TestRestTemplate restTemplate;

	@Test
	void shouldReturnACashCardWhenDataIsSaved(){
		ResponseEntity<String> response = restTemplate
				.getForEntity("/cashcards/99", String.class); /*HTTP GET request to app endpoint */

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK); /*compare code of response*/

		DocumentContext documentContext = JsonPath.parse(response.getBody()); /*convert string response to JSON*/
		Number id = documentContext.read("$.id");
		assertThat(id).isEqualTo(99);

		Double amount = documentContext.read("$.amount");
		assertThat(amount).isEqualTo(123.45);
	}

	@Test
	void shouldNotReturnACashCardWithAnUnknownId() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards/1000", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isBlank();
	}

	@Test
	void contextLoads() {
	}

}

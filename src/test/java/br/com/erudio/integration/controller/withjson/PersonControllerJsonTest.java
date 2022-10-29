package br.com.erudio.integration.controller.withjson;

import br.com.erudio.config.TestConfig;
import br.com.erudio.integration.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integration.vo.AccountCredentialsVO;
import br.com.erudio.integration.vo.PersonVO;
import br.com.erudio.integration.vo.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {
	
	static RequestSpecification specification;
	static ObjectMapper objectMapper;

	static PersonVO person;
	
	@BeforeAll
	static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		person = new PersonVO();
	}

	@Test
	@Order(0)
	void authorization() {

		var user = new AccountCredentialsVO("leandro", "admin123");

		var accessToken = given()
					.basePath("/auth/signin")
					.port(TestConfig.SERVER_PORT)
					.contentType(ContentType.JSON)
					.body(user)
				.when()
					.post()
				.then()
					.statusCode(HttpStatus.OK.value())
					.extract()
						.body()
							.as(TokenVO.class)
						.getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(HttpHeaders.AUTHORIZATION, TestConfig.BEARER + accessToken)
				.setBasePath("/api/person/v1")
				.setPort(TestConfig.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@Test
	@Order(1)
	void testCreate() throws JsonProcessingException {
		mockPerson();
		
		var content = given().spec(specification)
					.contentType(ContentType.JSON)
					.header(HttpHeaders.ORIGIN, TestConfig.ORIGIN_ERUDIO)
					.body(person)
				.when()
					.post()
				.then()
					.statusCode(HttpStatus.CREATED.value())
					.extract()
						.body()
							.asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		
		assertTrue(persistedPerson.getId() > 0);
		
		assertEquals("Richard", persistedPerson.getFirstName());
		assertEquals("Stallman", persistedPerson.getLastName());
		assertEquals("New York City, New York, US", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(2)
	void testCreateWithWrongOrigin() {
		mockPerson();

		var content = given().spec(specification)
				.contentType(ContentType.JSON)
				.header(HttpHeaders.ORIGIN, TestConfig.ORIGIN_SEMERU)
					.body(person)
				.when()
					.post()
				.then()
					.statusCode(403)
					.extract()
						.body()
							.asString();
		
		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

	@Test
	@Order(3)
	void testFindById() throws JsonProcessingException {
		mockPerson();

		var content = given().spec(specification)
					.contentType(ContentType.JSON)
					.header(HttpHeaders.ORIGIN, TestConfig.ORIGIN_ERUDIO)
					.pathParam("id", person.getId())
				.when()
					.get("{id}")
				.then()
					.statusCode(200)
					.extract()
						.body()
							.asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		
		assertTrue(persistedPerson.getId() > 0);
		
		assertEquals("Richard", persistedPerson.getFirstName());
		assertEquals("Stallman", persistedPerson.getLastName());
		assertEquals("New York City, New York, US", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}
	

	@Test
	@Order(4)
	void testFindByIdWithWrongOrigin() {
		mockPerson();

		var content = given().spec(specification)
					.contentType(ContentType.JSON)
					.header(HttpHeaders.ORIGIN, TestConfig.ORIGIN_SEMERU)
					.pathParam("id", person.getId())
				.when()
					.get("{id}")
				.then()
					.statusCode(403)
					.extract()
						.body()
							.asString();

		
		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}
	
	private void mockPerson() {
		person.setFirstName("Richard");
		person.setLastName("Stallman");
		person.setAddress("New York City, New York, US");
		person.setGender("Male");
	}

}

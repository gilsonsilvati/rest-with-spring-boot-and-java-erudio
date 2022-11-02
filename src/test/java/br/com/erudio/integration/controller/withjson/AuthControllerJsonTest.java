package br.com.erudio.integration.controller.withjson;

import br.com.erudio.config.TestConfig;
import br.com.erudio.integration.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integration.vo.AccountCredentialsVO;
import br.com.erudio.integration.vo.TokenVO;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerJsonTest extends AbstractIntegrationTest {

    static TokenVO tokenVO;

    @Test
    @Order(1)
    void testSignin() {

        var user = new AccountCredentialsVO("leandro", "admin123");

        tokenVO = given()
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
                            .as(TokenVO.class);

        assertNotNull(tokenVO);
        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
    }

    @Test
    @Order(2)
    void testRefresh() {

        var newTokenVO = given()
                    .basePath("/auth/refresh")
                    .port(TestConfig.SERVER_PORT)
                    .contentType(ContentType.JSON)
                    .pathParam("username", tokenVO.getUsername())
                    .header(HttpHeaders.AUTHORIZATION, TestConfig.BEARER + tokenVO.getRefreshToken())
                .when()
                    .put("{username}")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                        .body()
                            .as(TokenVO.class);

        assertNotNull(newTokenVO);
        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
    }
}

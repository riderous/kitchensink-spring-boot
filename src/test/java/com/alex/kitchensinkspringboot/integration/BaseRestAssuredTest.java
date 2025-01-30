package com.alex.kitchensinkspringboot.integration;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // To allow @BeforeAll without static
class BaseRestAssuredTest {

    @Value("${test.api.base-url:http://localhost:8080/api}")
    private String baseUrl;

    @BeforeAll
    void configureRestAssured() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        System.out.println("Running tests against: " + baseUrl);
    }
}
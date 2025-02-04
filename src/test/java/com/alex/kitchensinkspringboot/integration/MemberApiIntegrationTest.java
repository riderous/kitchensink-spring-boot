package com.alex.kitchensinkspringboot.integration;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Tag("integration")
class MemberApiIntegrationTest extends BaseRestAssuredTest {

    @Test
    void testCreateAndRetrieveMember() {
        // Create a new member
        String memberEmail = createMember("Alice Brown", "5551234567");

        // Retrieve the member by ID
        List<Map<String, Object>> members = given()
                .when()
                .get("/members")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList("$");

        String memberId = members.stream()
                .filter(member -> memberEmail.equals(String.valueOf(member.get("email"))))
                .map(member -> member.get("id").toString())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Member not found with email: " + memberEmail));

        given()
                .when()
                .get("/members/" + memberId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Alice Brown"))
                .body("phoneNumber", equalTo("5551234567"))
                .body("email", equalTo(memberEmail));
    }

    @Test
    void testListAllMembers() {
        // Create multiple members
        createMember("John Doe", "1234567890");
        createMember("Jane Smith", "9876543210");

        // Verify all members are returned
        given()
                .when()
                .get("/members")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThanOrEqualTo(2))
                .body("name", hasItem("John Doe"))
                .body("name", hasItem("Jane Smith"));
    }

    @Test
    void testInvalidInput() {
        // Attempt to create a member with invalid input
        String invalidMemberJson = """
                {
                    "name": "",
                    "email": "invalid-email",
                    "phoneNumber": "123"
                }
                """;

        given()
                .contentType("application/json")
                .body(invalidMemberJson)
                .when()
                .post("/members")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("name", containsString("size must be between 1 and 25"))
                .body("phoneNumber", containsString("size must be between 10 and 12"))
                .body("email", containsString("must be a well-formed email address"));
    }

    @Test
    void testInvalidName() {
        // Attempt to create a member with invalid input
        String invalidMemberJson = """
                {
                    "name": "alice123",
                    "email": "alice@example.com",
                    "phoneNumber": "5551234567"
                }
                """;

        given()
                .contentType("application/json")
                .body(invalidMemberJson)
                .when()
                .post("/members")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("name", containsString("Must not contain numbers"));
    }

    @Test
    void testDuplicateEmail() {
        String memberEmail = createMember("Alice Brown", "5551234567");
        String duplicateMemberJson = """
                {
                    "name": "Alice Brown",
                    "email": "%s",
                    "phoneNumber": "5551234567"
                }
                """.formatted(memberEmail);
        // expect 400 in the new code for simplicity
        given()
                .contentType("application/json")
                .body(duplicateMemberJson)
                .when()
                .post("/members")
                .then()
                .statusCode(anyOf(is(HttpStatus.CONFLICT.value()), is(HttpStatus.BAD_REQUEST.value())))
                .body("email", containsString("Email taken"));
    }

    // Utility Method to Create Members
    private String createMember(String name, String phoneNumber) {
        String email = "%s+%s@example.com".formatted(
                name.toLowerCase().replace(' ', '.'),
                System.currentTimeMillis());

        String memberJson = """
                {
                    "name": "%s",
                    "email": "%s",
                    "phoneNumber": "%s"
                }
                """.formatted(name, email, phoneNumber);

        given()
                .contentType("application/json")
                .body(memberJson)
                .when()
                .post("/members")
                .then()
                .statusCode(HttpStatus.OK.value());
        return email;
    }
}

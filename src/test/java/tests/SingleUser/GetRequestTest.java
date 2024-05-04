package tests.SingleUser;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.BaseTest;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specs.requestSpec;
import static specs.Specs.responseSpec;

@Epic("Users")
@Feature("Get user by username")
public class GetRequestTest extends BaseTest {
    User testUser = new User(666, "Quattro", "Sam", "Something", "gmail@gmail.com", "qwerty123", "88805050707", 3);

    @BeforeEach
    public void createTestUser() {
        RestAssured.given()
                .spec(requestSpec())
                .body(testUser)
                .post("user/");
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Get user by username without pojo")
    public void getUser() {
        RestAssured.given()
                .spec(requestSpec())
                .get(String.format("user/%s", testUser.username()))
                .then()
                .spec(responseSpec(200))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("singleUserSchema.json"))
                .body("id", equalTo(testUser.id()))
                .body("username", equalTo(testUser.username()))
                .body("firstName", equalTo(testUser.firstName()))
                .body("lastName", equalTo(testUser.lastName()))
                .body("email", equalTo(testUser.email()))
                .body("password", equalTo(testUser.password()))
                .body("phone", equalTo(testUser.phone()))
                .body("userStatus", equalTo(testUser.userStatus()));
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Get user by username using pojo")
    public void getUserWithPojo() {
        User userResponse = RestAssured.given()
                .spec(requestSpec())
                .get(String.format("user/%s", testUser.username()))
                .then()
                .spec(responseSpec(200))
                .body(matchesJsonSchemaInClasspath("singleUserSchema.json"))
                .extract().as(User.class);
        assertEquals(userResponse.id(), testUser.id());
        assertEquals(userResponse.username(), testUser.username());
        assertEquals(userResponse.firstName(), testUser.firstName());
        assertEquals(userResponse.lastName(), testUser.lastName());
        assertEquals(userResponse.email(), testUser.email());
        assertEquals(userResponse.password(), testUser.password());
        assertEquals(userResponse.phone(), testUser.phone());
        assertEquals(userResponse.userStatus(), testUser.userStatus());
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Get a non-existing user user by username")
    public void getNonExistingUser() {
        RestAssured.given()
                .spec(requestSpec())
                .pathParam("username", "useruseruser")
                .get("user/{username}")
                .then()
                .spec(responseSpec(404))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("infoResponseSchema.json"))
                .body("message", equalTo("User not found"))
                .body("type", equalTo("error"));
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Get invalid user user by username")
    public void getTooLargeUser() {
        String largeUsername = generateLongString(9000);
        RestAssured.given()
                .spec(requestSpec())
                .pathParam("username", largeUsername)
                .get("user/{username}")
                .then()
                .assertThat()
                .statusCode(414);
    }

//    @Test
//    @Severity(SeverityLevel.BLOCKER)
//    @DisplayName("Get invalid user user by username")
//    public void getInvalidUser() {
//        RestAssured.given()
//                .spec(requestSpec())
//                .pathParam("username", 12.2f)
//                .get("user/{username}")
//                .then()
//                .spec(responseSpec(400))
//                .log().all();
//    }

    private String generateLongString(int length) {
        return "a".repeat(Math.max(0, length));
    }
}

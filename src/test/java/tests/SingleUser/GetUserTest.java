package tests.SingleUser;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.BaseTest;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specs.requestSpec;
import static specs.Specs.responseSpec;

@Epic("Users")
@Feature("Get user by username")
public class GetUserTest extends BaseTest {
    User testUser = new User(666, "Quattro", "Sam", "Something", "gmail@gmail.com", "qwerty123", "88805050707", 3);

    @BeforeEach
    public void createTestUser() {
        given()
                .spec(requestSpec())
                .body(testUser)
                .post("user/");
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Get user by username without pojo")
    public void getUser() {
        given()
                .spec(requestSpec())
                .pathParam("username", testUser.username())
                .get("user/{username}")
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
        User userResponse = given()
                .spec(requestSpec())
                .pathParam("username", testUser.username())
                .get("user/{username}")
                .then()
                .spec(responseSpec(200))
                .body(matchesJsonSchemaInClasspath("singleUserSchema.json"))
                .extract().as(User.class);
        assertEquals(userResponse.id(), testUser.id(), "id is not as expected");
        assertEquals(userResponse.username(), testUser.username(), "username is not as expected");
        assertEquals(userResponse.firstName(), testUser.firstName(), "firstname is not as expected");
        assertEquals(userResponse.lastName(), testUser.lastName(), "lastname is not as expected");
        assertEquals(userResponse.email(), testUser.email(), "email is not as expected");
        assertEquals(userResponse.password(), testUser.password(), "password is not as expected");
        assertEquals(userResponse.phone(), testUser.phone(), "phone is not as expected");
        assertEquals(userResponse.userStatus(), testUser.userStatus(), "usersatus is not as expected");
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Get a non-existing user user by username")
    public void getNonExistingUser() {
        given()
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
        given()
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
//        given()
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

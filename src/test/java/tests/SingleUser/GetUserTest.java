package tests.SingleUser;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Users")
@Feature("Get user by username")
public class GetUserTest extends UserBaseTest {
    User testUser = new User(666, "Quattro", "Sam", "Something", "gmail@gmail.com", "qwerty123", "88805050707", 3);

    @BeforeEach
    public void createTestUser() {
        given()
                .spec(requestSpec())
                .body(testUser)
                .post(apiProperties().getProperty("api.user.base"));
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Get user by username without pojo")
    public void getUser() {
        given()
                .spec(requestSpec())
                .pathParam("username", testUser.getUsername())
                .get(apiProperties().getProperty("api.user.base") + "{username}")
                .then()
                .spec(responseSpec(200))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("singleUserSchema.json"))
                .body("id", equalTo(testUser.getId()))
                .body("username", equalTo(testUser.getUsername()))
                .body("firstName", equalTo(testUser.getFirstName()))
                .body("lastName", equalTo(testUser.getLastName()))
                .body("email", equalTo(testUser.getEmail()))
                .body("password", equalTo(testUser.getPassword()))
                .body("phone", equalTo(testUser.getPhone()))
                .body("userStatus", equalTo(testUser.getUserStatus()));
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Get user by username using pojo")
    public void getUserWithPojo() {
        User userResponse = given()
                .spec(requestSpec())
                .pathParam("username", testUser.getUsername())
                .get(apiProperties().getProperty("api.user.base") + "{username}")
                .then()
                .spec(responseSpec(200))
                .body(matchesJsonSchemaInClasspath("singleUserSchema.json"))
                .extract().as(User.class);

        assertEquals(userResponse.getId(), testUser.getId(), "id is not as expected");
        assertEquals(userResponse.getUsername(), testUser.getUsername(), "username is not as expected");
        assertEquals(userResponse.getFirstName(), testUser.getFirstName(), "firstname is not as expected");
        assertEquals(userResponse.getLastName(), testUser.getLastName(), "lastname is not as expected");
        assertEquals(userResponse.getEmail(), testUser.getEmail(), "email is not as expected");
        assertEquals(userResponse.getPassword(), testUser.getPassword(), "password is not as expected");
        assertEquals(userResponse.getPhone(), testUser.getPhone(), "phone is not as expected");
        assertEquals(userResponse.getUserStatus(), testUser.getUserStatus(), "usersatus is not as expected");
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Get a non-existing user user by username")
    public void getNonExistingUser() {
        given()
                .spec(requestSpec())
                .pathParam("username", "useruseruser")
                .get(apiProperties().getProperty("api.user.base") + "{username}")
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
                .get(apiProperties().getProperty("api.user.base") + "{username}")
                .then()
                .assertThat()
                .statusCode(414);
    }

    //  generating a string of a given length
    private String generateLongString(int length) {
        return "a".repeat(Math.max(0, length));
    }
}

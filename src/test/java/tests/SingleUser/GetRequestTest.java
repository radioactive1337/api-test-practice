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
import static specs.Specs.requestSpec;
import static specs.Specs.responseSpec;

@Epic("Users")
@Feature("Get user")
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
    @DisplayName("Get user by username smoke test")
    public void getUser() {
        RestAssured.given()
                .spec(requestSpec())
                .get(String.format("user/%s", testUser.username()))
                .then()
                .spec(responseSpec(200))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("singleUserSchema.json"))
                .body("id", equalTo(666))
                .body("username", equalTo(testUser.username()))
                .body("firstName", equalTo(testUser.firstName()))
                .body("lastName", equalTo(testUser.lastName()))
                .body("email", equalTo(testUser.email()))
                .body("password", equalTo(testUser.password()))
                .body("phone", equalTo(testUser.phone()))
                .body("userStatus", equalTo(testUser.userStatus()));
    }

    //  test with extraction to class
    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Get user by username smoke test")
    public void getUserWithPojo() {

    }
}

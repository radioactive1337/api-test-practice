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

@Epic("Users")
@Feature("Delete user by username")
public class DeleteUserTest extends UserBaseTest {
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
    @DisplayName("Delete user by username")
    public void deleteUser() {
        given()
                .spec(requestSpec())
                .delete(apiProperties().getProperty("api.user.base") + "{username}", testUser.username())
                .then()
                .spec(responseSpec(200))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("infoResponseSchema.json"))
                .body("message", equalTo(testUser.username()))
                .body("code", equalTo(200));
    }
}

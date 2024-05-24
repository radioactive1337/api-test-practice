package tests.SingleUser;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@Epic("Users")
@Feature("Update user")
public class UpdateUserTest extends UserBaseTest {
    User testUser = new User(12345, "Quattro", "Sam", "Something", "gmail@gmail.com", "qwerty123", "88805050707", 3);

    @BeforeEach
    public void createTestUser() {
        given()
                .spec(requestSpec())
                .body(testUser)
                .post(apiProperties().getProperty("api.user.base"));
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Update user using pojo")
    public void updateUserWithPojo() {
        User newUser = new User(111, "nickname", "Sam", "Altman", "mail@mail.com", "qwerty123", "88805050707", 1);
        given()
                .spec(requestSpec())
                .body(newUser)
                .pathParam("username", testUser.getUsername())
                .put(apiProperties().getProperty("api.user.base") + "{username}")
                .then()
                .spec(responseSpec(200))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("infoResponseSchema.json"))
                .body("message", equalTo(newUser.getId().toString()))
                .body("code", equalTo(200));
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Update user without pojo")
    public void updateUser() {
        Map<String, Object> newUser = Map.of("id", 222,
                "username", "nickname",
                "firstName", "Sam",
                "lastName", "Altman",
                "email", "mail@mail.com",
                "password", "qwerty123",
                "phone", "88805050707",
                "userStatus", "1");
        given()
                .spec(requestSpec())
                .body(newUser)
                .pathParam("username", testUser.getUsername())
                .put(apiProperties().getProperty("api.user.base") + "{username}")
                .then()
                .spec(responseSpec(200))
                .body(matchesJsonSchemaInClasspath("infoResponseSchema.json"))
                .body("message", equalTo(newUser.get("id").toString()))
                .body("code", equalTo(200));
    }
}

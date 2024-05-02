package tests.SingleUser;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.BaseTest;

import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static specs.Specs.requestSpec;
import static specs.Specs.responseSpec;

@Epic("Users")
@Feature("Create user")
public class PostRequestTest extends BaseTest {
    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Create user smoke test")
    public void createUser() {
        HashMap<String, Object> testUser1 = new HashMap<>();
        testUser1.put("id", 123);
        testUser1.put("username", "nickname");
        testUser1.put("firstName", "Sam");
        testUser1.put("lastName", "Altman");
        testUser1.put("email", "mail@mail.com");
        testUser1.put("password", "qwerty123");
        testUser1.put("phone", "88805050707");
        testUser1.put("userStatus", "1");
        RestAssured.given()
                .spec(requestSpec())
                .body(testUser1)
                .post("user/")
                .then()
                .spec(responseSpec(200))
                .assertThat()
                .body("message", equalTo(testUser1.get("id").toString()))
                .body("code", equalTo(200));
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Create user smoke test")
    public void createUserWithPojoClass() {
        User testUser1 = new User(123, "nickname", "Sam", "Altman", "mail@mail.com", "qwerty123", "88805050707", 1);
        RestAssured.given()
                .spec(requestSpec())
                .body(testUser1)
                .post("user/")
                .then()
                .spec(responseSpec(200))
                .assertThat()
                .body("message", equalTo(testUser1.id().toString()))
                .body("code", equalTo(200));
    }
}

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

import static org.hamcrest.Matchers.is;
import static specs.Specs.requestSpec;
import static specs.Specs.responseSpec;

@Epic("Users")
@Feature("Create user")
public class PostRequestTest extends BaseTest {
    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("User create testing (POST)")
    public void createUser() {
        User t1 = new User(123, "nickname", "Sam", "Altman", "mail@mail.com", "qwerty123", "88805050707", 1);
        RestAssured.given()
                .spec(requestSpec())
                .body(t1)
                .post("user")
                .then()
                .spec(responseSpec(200))
                .assertThat()
                .body("message", is(t1.id().toString()));
    }
}

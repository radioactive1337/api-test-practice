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
    public void postUser() {
        RestAssured.given()
                .spec(requestSpec())
                .body(new User("Cloud", "AQA"))
                .post("api/users")
                .then()
                .spec(responseSpec(201))
                .assertThat()
                .body("name", is("Cloud"))
                .body("job", is("AQA"));
    }

}

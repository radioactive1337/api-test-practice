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

import static specs.Specs.requestSpec;
import static specs.Specs.responseSpec;

@Epic("Users")
@Feature("Update user")
public class PutRequestTest extends BaseTest {
    private int id;

//    @BeforeEach
//    public void setupForUpdateUserTest() {
//        User user = new User("Cactus", "Cleaner");
//        id = Integer.parseInt(RestAssured.given()
//                .spec(requestSpec())
//                .body(user)
//                .post("api/users")
//                .then()
//                .extract()
//                .response().jsonPath().get("id"));
//    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("User update testing (PUT)")
    public void updateUser() {
//        RestAssured.given()
//                .spec(requestSpec())
//                .body(new User("Cactus", "Cleaner"))
//                .put("api/users/1")
//                .then()
//                .spec(responseSpec(200));
    }
}

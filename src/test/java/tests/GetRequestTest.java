package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static specs.Specs.requestSpec;
import static specs.Specs.responseSpecGetOk;

@Epic("Users")
@Feature("Get single user")
public class GetRequestTest extends BaseTest {
    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Request testing GET single user")
    public void getSingleUser() {
        RestAssured.given()
                .spec(requestSpec())
                .get("/api/users/2")
                .then()
                .spec(responseSpecGetOk())
                .assertThat()
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("data.avatar", containsString("2"))
                .body(matchesJsonSchemaInClasspath("singleUserSchema.json"));
    }
}

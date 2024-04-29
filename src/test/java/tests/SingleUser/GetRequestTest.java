package tests.SingleUser;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import tests.BaseTest;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static specs.Specs.requestSpec;
import static specs.Specs.responseSpec;

@Epic("Users")
@Feature("Get user")
public class GetRequestTest extends BaseTest {
    @ParameterizedTest
    @CsvSource(value = {
            "2, janet.weaver@reqres.in, Janet, Weaver",
            "4, eve.holt@reqres.in, Eve, Holt"
    })
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Get user testing (GET)")
    public void getUser(int id, String email, String first_name, String second_name) {
        RestAssured.given()
                .spec(requestSpec())
                .get(String.format("api/users/%d", id))
                .then()
                .spec(responseSpec(200))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("singleUserSchema.json"))
                .body("data.id", is(id))
                .body("data.email", is(email))
                .body("data.first_name", is(first_name))
                .body("data.last_name", is(second_name))
                .body("data.avatar", containsString(String.format("%d", id)));
    }
}

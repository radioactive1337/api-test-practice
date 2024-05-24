package tests.SingleUser;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tools.ExcelHelper;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@Epic("Users")
@Feature("Create user")
public class CreateUserTest extends UserBaseTest {

    private final String userEndpoint = apiProperties().getProperty("api.user.base");

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Create user without pojo")
    public void createUser() {
        Map<String, Object> testUser1 = Map.of(
                "id", 123,
                "username", "nickname",
                "firstName", "Sam",
                "lastName", "Altman",
                "email", "mail@mail.com",
                "password", "qwerty123",
                "phone", "88805050707",
                "userStatus", 1
        );
        given()
                .spec(requestSpec())
                .body(testUser1)
                .post(userEndpoint)
                .then()
                .spec(responseSpec(200))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("infoResponseSchema.json"))
                .body("message", equalTo(testUser1.get("id").toString()))
                .body("code", equalTo(200));
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Create user using pojo")
    public void createUserWithPojoClass() {
        User testUser1 = new User(123, "nickname", "Sam", "Altman", "mail@mail.com", "qwerty123", "88805050707", 1);
        given()
                .spec(requestSpec())
                .body(testUser1)
                .post(userEndpoint)
                .then()
                .spec(responseSpec(200))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("infoResponseSchema.json"))
                .body("message", equalTo(testUser1.getId().toString()))
                .body("code", equalTo(200));
    }

    @ParameterizedTest
    @MethodSource("provideUsers")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Create user using xlsx")
    public void createUserWithXlsx(User testUser) {
        given()
                .spec(requestSpec())
                .body(testUser)
                .post(userEndpoint)
                .then()
                .spec(responseSpec(200))
                .body(matchesJsonSchemaInClasspath("infoResponseSchema.json"))
                .body("message", equalTo(testUser.getId().toString()))
                .body("code", equalTo(200));
    }

    private static Stream<User> provideUsers() {
        List<Object[]> rawData = ExcelHelper.readExcelFile("src/test/resources/testdata.xlsx");
        return rawData.stream().map(data -> new User(
                //  cast each field appropriately
                ((Double) data[0]).intValue(), //   convert Double to int
                String.valueOf(data[1]),
                String.valueOf(data[2]),
                String.valueOf(data[3]),
                String.valueOf(data[4]),
                String.valueOf(data[5]),
                String.valueOf(data[6]).split("\\.")[0],
                ((Double) data[7]).intValue() //    convert Double to int
        ));
    }

}

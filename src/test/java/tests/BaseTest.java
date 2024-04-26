package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {
    @BeforeAll
    public void setup() {
        RestAssured.filters(new AllureRestAssured());
    }

    @AfterAll
    public void tearDown() {
        RestAssured.reset();
    }
}

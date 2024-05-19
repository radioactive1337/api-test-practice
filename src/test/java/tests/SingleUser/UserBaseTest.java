package tests.SingleUser;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.http.ContentType.JSON;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.lessThanOrEqualTo;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserBaseTest {

    //  setting up log config
    private final LogConfig logCfg = LogConfig
            .logConfig()
            .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);

    //  log config -> rest assured config
    private final RestAssuredConfig cfg = RestAssuredConfig
            .config()
            .logConfig(logCfg);

    //  setting up request config
    public RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(apiProperties().getProperty("api.base_url"))
                .setRelaxedHTTPSValidation()
                .setContentType(JSON)
                .setAccept(JSON)
                .log(LogDetail.BODY)
                .setConfig(cfg)
                .build();
    }

    //  setting up response config
    public ResponseSpecification responseSpec(int status) {
        return new ResponseSpecBuilder()
                .log(LogDetail.STATUS)
                .expectContentType(JSON)
                .expectStatusCode(status)
                .expectResponseTime(lessThanOrEqualTo(3L), SECONDS)
                .build();
    }

    //  extract api properties from .properties file
    public Properties apiProperties() {
        Properties properties = new Properties();
        try (FileReader fileReader = new FileReader("src/test/resources/config.properties")) {
            properties.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    @BeforeAll
    public void setup() {
        RestAssured.filters(new AllureRestAssured());
    }

    @AfterAll
    public void tearDown() {
        RestAssured.reset();
    }
}

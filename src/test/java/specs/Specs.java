package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.http.ContentType.JSON;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class Specs {
    private static final LogConfig logCfg = LogConfig
            .logConfig()
            .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);

    private static final RestAssuredConfig cfg = RestAssuredConfig
            .config()
            .logConfig(logCfg);

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri("https://reqres.in/")
                .setRelaxedHTTPSValidation()
                .setContentType(JSON)
                .setAccept(JSON)
                .setConfig(cfg)
                .build();
    }

    public static ResponseSpecification responseSpecGetOk() {
        return new ResponseSpecBuilder()
                .log(LogDetail.STATUS)
                .expectContentType(JSON)
                .expectStatusCode(200)
                .expectResponseTime(lessThanOrEqualTo(3L), SECONDS)
                .build();
    }
}


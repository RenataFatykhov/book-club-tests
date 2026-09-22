package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static allure.CustomAllureListener.withCustomTemplate;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;

public class BaseSpec {
    public static RequestSpecification requestSpec = with()
            .filter(withCustomTemplate())
            .log().all()
            .contentType("application/json");

    public static RequestSpecification requestWithoutContentTypeSpec = with()
            .filter(withCustomTemplate())
            .log().all();

    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .build();

    public static ResponseSpecBuilder baseResponseSpec() {
        return new ResponseSpecBuilder()
                .addResponseSpecification(responseSpec);
    }
}

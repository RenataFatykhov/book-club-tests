package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;

public class BaseSpec {
    public static RequestSpecification requestSpec = with()
            .log().all()
            .contentType("application/json")
            .basePath("/api/v1");

    public static RequestSpecification requestWithoutContentTypeSpec = with()
            .log().all()
            .basePath("/api/v1");

    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .build();

    public static ResponseSpecBuilder baseResponseSpec() {
        return new ResponseSpecBuilder()
                .addResponseSpecification(responseSpec);
    }
}

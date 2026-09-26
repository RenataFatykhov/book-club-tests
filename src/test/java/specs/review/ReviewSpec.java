package specs.review;

import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseResponseSpec;

public class ReviewSpec {

    public static ResponseSpecification getReviewResponseSpec = baseResponseSpec()
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("schemas/reviews/get_reviews_response_schema.json"))
            .expectBody("count", notNullValue())
            .expectBody("results", notNullValue())
            .build();
}

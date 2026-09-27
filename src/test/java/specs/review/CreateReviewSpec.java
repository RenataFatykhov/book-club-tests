package specs.review;

import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseResponseSpec;

public class CreateReviewSpec {

    public static ResponseSpecification successfulCreateReviewResponseSpec = baseResponseSpec()
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath("schemas/reviews/post_reviews_response_schema.json"))
            .expectBody("review", notNullValue())
            .expectBody("assessment", notNullValue())
            .build();

    public static ResponseSpecification unauthorizedReviewResponseSpec = baseResponseSpec()
            .expectStatusCode(401)
            .expectBody("detail", notNullValue())
            .build();
}

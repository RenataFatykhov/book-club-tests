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

    public static ResponseSpecification getReviewByIdResponseSpec = baseResponseSpec()
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("schemas/reviews/get_reviews_by_id_response_schema.json"))
            .expectBody("id", notNullValue())
            .expectBody("club", notNullValue())
            .build();

    public static ResponseSpecification notFoundReviewResponseSpec = baseResponseSpec()
            .expectStatusCode(404)
            .expectBody(matchesJsonSchemaInClasspath("schemas/reviews/get_reviews_by_new_book_id_response_schema.json"))
            .expectBody("detail", notNullValue())
            .build();

    public static ResponseSpecification successfulDeleteReviewResponseSpec = baseResponseSpec()
            .expectStatusCode(204)
            .build();
}

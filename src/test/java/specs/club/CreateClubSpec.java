package specs.club;

import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseResponseSpec;

public class CreateClubSpec {

    public static ResponseSpecification successfulCreateClubResponseSpec = baseResponseSpec()
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath("schemas/club/create_club_response_sсhema.json"))
            .expectBody("bookTitle", notNullValue())
            .expectBody("bookAuthors", notNullValue())
            .build();

    public static ResponseSpecification existingCreateClubResponseSpec = baseResponseSpec()
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath("schemas/club/existing_create_club_response_schema.json"))
            .expectBody("bookTitle", notNullValue())
            .build();
}

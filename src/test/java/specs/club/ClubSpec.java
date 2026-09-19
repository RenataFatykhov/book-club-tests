package specs.club;

import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseResponseSpec;

public class ClubSpec {

    public static ResponseSpecification getClubResponseSpec = baseResponseSpec()
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("schemas/club/get_club_response_schema.json"))
            .expectBody("count", notNullValue())
            .expectBody("results", notNullValue())
            .build();
}

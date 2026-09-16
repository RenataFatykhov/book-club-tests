package specs.registration;

import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseResponseSpec;

public class RegistrationSpec {
    public static ResponseSpecification successRegistrationResponseSpec = baseResponseSpec()
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/registration/successful_registration_response_schema.json"))
            .expectBody("id", notNullValue())
            .expectBody("username", notNullValue())
            .expectBody("remoteAddr", notNullValue())
            .build();

    public static ResponseSpecification error400RegistrationResponseSpec = baseResponseSpec()
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/registration/400_registration_response_schema.json"))
            .expectBody("username", notNullValue())
            .build();

    public static ResponseSpecification error415RegistrationResponseSpec = baseResponseSpec()
            .expectStatusCode(415)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/registration/415_registration_response_schema.json"))
            .build();

    public static ResponseSpecification redirect301RegistrationResponseSpec = baseResponseSpec()
            .expectStatusCode(301)
            .build();
}

package specs.login;

import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseResponseSpec;

public class LoginSpec {
    public static ResponseSpecification successLoginResponseSpec = baseResponseSpec()
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("schemas/login/successful_login_response_schema.json"))
            .expectBody("access", notNullValue())
            .expectBody("refresh", notNullValue())
            .build();

    public static ResponseSpecification wrongCredentialsLoginResponseSpec = baseResponseSpec()
            .expectStatusCode(401)
            .expectBody(matchesJsonSchemaInClasspath("schemas/login/wrong_credentials_login_response_schema.json"))
            .expectBody("detail", notNullValue())
            .build();

    public static ResponseSpecification emptyUsernameLoginResponseSpec = baseResponseSpec()
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath("schemas/login/empty_username_login_response_schema.json"))
            .expectBody("username", notNullValue())
            .build();

    public static ResponseSpecification emptyPasswordLoginResponseSpec = baseResponseSpec()
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath("schemas/login/empty_password_login_response_schema.json"))
            .expectBody("password", notNullValue())
            .build();
}

package tests;

import models.registration.lombok.RegistrationBodyLombokModel;
import models.registration.lombok.RegistrationResponseLombokModel;
import models.registration.pojo.RegistrationBodyPojoModel;
import models.registration.pojo.RegistrationResponsePojoModel;
import models.registration.records.ErrorResponseRecordsModel;
import models.registration.records.RegistrationBodyRecordsModel;
import models.registration.records.RegistrationResponseRecordsModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class RegistrationTests extends TestBase {
    String username;
    String password;
    String invalidUsername;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        username = faker.name().firstName();
        password = faker.credentials().password();
        invalidUsername = faker.name().fullName();
    }

    @Test
    public void successfulRegistrationTest_with_pojo() {

        RegistrationBodyPojoModel registrationData = new RegistrationBodyPojoModel();
        registrationData.setUsername(username);
        registrationData.setPassword(password);

        RegistrationResponsePojoModel registrationResponse = given()
                .log().all()
                .contentType("application/json")
                .body(registrationData)
                .basePath("/api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("username", notNullValue())
                .body("remoteAddr", notNullValue())
                .body(matchesJsonSchemaInClasspath(
                        "schemas/registration/successful_registration_response_schema.json"))
                .extract()
                .as(RegistrationResponsePojoModel.class);

        String actualUsername = registrationResponse.getUsername();
        assertThat(actualUsername).isEqualTo(username);

    }

    @Test
    public void successfulRegistrationTest_with_lombok() {

        RegistrationBodyLombokModel registrationData = new RegistrationBodyLombokModel();
        registrationData.setUsername(username);
        registrationData.setPassword(password);

        RegistrationResponseLombokModel registrationResponse = given()
                .log().all()
                .contentType("application/json")
                .body(registrationData)
                .basePath("/api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("username", notNullValue())
                .body("remoteAddr", notNullValue())
                .body(matchesJsonSchemaInClasspath(
                        "schemas/registration/successful_registration_response_schema.json"))
                .extract()
                .as(RegistrationResponseLombokModel.class);

        String actualUsername = registrationResponse.getUsername();
        assertThat(actualUsername).isEqualTo(username);


    }

    @Test
    public void successfulRegistrationTest_with_records() {

        RegistrationBodyRecordsModel registrationData = new RegistrationBodyRecordsModel(username, password);

        RegistrationResponseRecordsModel registrationResponse = given()
                .log().all()
                .contentType("application/json")
                .body(registrationData)
                .basePath("/api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("username", notNullValue())
                .body("remoteAddr", notNullValue())
                .body(matchesJsonSchemaInClasspath(
                        "schemas/registration/successful_registration_response_schema.json"))
                .extract()
                .as(RegistrationResponseRecordsModel.class);

        assertThat(registrationResponse.username()).isEqualTo(username);
        assertThat(registrationResponse.id()).isGreaterThan(0);
        assertThat(registrationResponse.firstName()).isEqualTo("");
        assertThat(registrationResponse.lastName()).isEqualTo("");
        assertThat(registrationResponse.email()).isEqualTo("");

        String ipAddrRegexp =
                "^(?:(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}"
                        + "(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";
        assertThat(registrationResponse.remoteAddr()).matches(ipAddrRegexp);

    }

    @Test
    public void existingUserWrongRegistrationTest() {

        RegistrationBodyRecordsModel registrationData = new RegistrationBodyRecordsModel(username, password);

        RegistrationResponseRecordsModel firstRegistrationResponse = given()
                .log().all()
                .contentType("application/json")
                .body(registrationData)
                .basePath("/api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath(
                        "schemas/registration/successful_registration_response_schema.json"))
                .body("username", is(username))
                .body("id", notNullValue())
                .extract()
                .as(RegistrationResponseRecordsModel.class);

        assertThat(firstRegistrationResponse.username()).isEqualTo(username);


        ErrorResponseRecordsModel secondRegistrationResponse = given()
                .log().all()
                .contentType("application/json")
                .body(registrationData)
                .basePath("/api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .body("username", notNullValue())
                .body(matchesJsonSchemaInClasspath(
                        "schemas/registration/400_registration_response_schema.json"))
                .extract()
                .as(ErrorResponseRecordsModel.class);

        String expectedErrorMessage = "A user with that username already exists.";
        String actualErrorMessage = secondRegistrationResponse.username().get(0);
        assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
    }

    @Test
    public void invalidUsername400Test() {

        RegistrationBodyRecordsModel registrationData = new RegistrationBodyRecordsModel(invalidUsername, password);

        ErrorResponseRecordsModel response = given()
                .log().all()
                .contentType("application/json")
                .body(registrationData)
                .basePath("/api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath(
                        "schemas/registration/400_registration_response_schema.json"))
                .extract()
                .as(ErrorResponseRecordsModel.class);

        String expectedErrorMessage =
                "Enter a valid username. This value may contain only letters, numbers, and @/./+/-/_ characters.";
        assertEquals(expectedErrorMessage, response.username().get(0));

    }

    @Test
    public void negativeRegistration415Test() {

        RegistrationBodyRecordsModel registrationData = new RegistrationBodyRecordsModel(username, password);

        given()
                .log().all()
                .body(registrationData)
                .basePath("/api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(415)
                .body(matchesJsonSchemaInClasspath(
                        "schemas/registration/415_registration_response_schema.json"));

    }

    @Test
    public void negativeRegistration301Test() {

        RegistrationBodyRecordsModel registrationData = new RegistrationBodyRecordsModel(username, password);

        given()
                .log().all()
                .body(registrationData)
                .basePath("/api/v1")
                .when()
                .post("/users/register")
                .then()
                .log().all()
                .statusCode(301);

    }
}

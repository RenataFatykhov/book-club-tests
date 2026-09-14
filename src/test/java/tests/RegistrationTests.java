package tests;

import models.registration.ErrorResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.requestWithoutContentTypeSpec;
import static specs.registration.RegistrationSpec.*;


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
    public void successfulRegistrationTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        RegistrationResponseModel registrationResponse = given(requestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successRegistrationResponseSpec)
                .extract()
                .as(RegistrationResponseModel.class);

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

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        RegistrationResponseModel firstRegistrationResponse = given(requestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successRegistrationResponseSpec)
                .body("username", is(username))
                .extract()
                .as(RegistrationResponseModel.class);

        assertThat(firstRegistrationResponse.username()).isEqualTo(username);


        ErrorResponseModel secondRegistrationResponse = given(requestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(error400RegistrationResponseSpec)
                .extract()
                .as(ErrorResponseModel.class);

        String expectedErrorMessage = "A user with that username already exists.";
        String actualErrorMessage = secondRegistrationResponse.username().get(0);
        assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
    }

    @Test
    public void invalidUsername400Test() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(invalidUsername, password);

        ErrorResponseModel response = given(requestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(error400RegistrationResponseSpec)
                .extract()
                .as(ErrorResponseModel.class);

        String expectedErrorMessage =
                "Enter a valid username. This value may contain only letters, numbers, and @/./+/-/_ characters.";
        assertEquals(expectedErrorMessage, response.username().get(0));

    }

    @Test
    public void negativeRegistration415Test() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        given(requestWithoutContentTypeSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(error415RegistrationResponseSpec);

    }

    @Test
    public void negativeRegistration301Test() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        given(requestWithoutContentTypeSpec)
                .body(registrationData)
                .when()
                .post("/users/register")
                .then()
                .spec(redirect301RegistrationResponseSpec);

    }
}

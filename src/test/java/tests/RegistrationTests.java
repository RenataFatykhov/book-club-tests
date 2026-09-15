package tests;

import models.registration.ErrorResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static data.TestData.*;
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
        username = setUsername();
        password = setPassword();
        invalidUsername = setInvalidUsername();
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
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
        assertThat(registrationResponse.remoteAddr()).matches(ipAddrRegexp);

    }

    @Test
    @DisplayName("Повторная регистрация на зарегистрированного юзера")
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

        String actualErrorMessage = secondRegistrationResponse.username().get(0);
        assertThat(actualErrorMessage).isEqualTo(expectedExistUserErrorMessage);
    }

    @Test
    @DisplayName("Ввод невалидного юзера")
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

        assertEquals(expectedInvalidUsernameErrorMessage, response.username().get(0));

    }

    @Test
    @DisplayName("Статус код 415")
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
    @DisplayName("Статус код 301")
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

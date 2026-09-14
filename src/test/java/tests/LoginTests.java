package tests;

import models.login.*;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.requestSpec;
import static specs.login.LoginSpec.*;
import static specs.registration.RegistrationSpec.successRegistrationResponseSpec;

public class LoginTests extends TestBase {
    String username;
    String password;
    String wrongPassword;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        username = faker.name().firstName();
        password = faker.credentials().password();
        wrongPassword = faker.credentials().password();
    }

    @Test
    public void successfulLoginTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        given(requestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successRegistrationResponseSpec)
                .extract()
                .as(RegistrationResponseModel.class);

        LoginRequestModel loginData = new LoginRequestModel(username, password);

        SuccessfulLoginReponseModel loginResponse = given(requestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successLoginResponseSpec)
                .extract().as(SuccessfulLoginReponseModel.class);

        String expectedTokenPath = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();

        assertThat(actualAccess).startsWith(expectedTokenPath);
        assertThat(actualRefresh).startsWith(expectedTokenPath);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    public void wrongCredentialsLoginTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        given(requestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successRegistrationResponseSpec)
                .extract()
                .as(RegistrationResponseModel.class);

        LoginRequestModel loginData = new LoginRequestModel(username, wrongPassword);

        WrongCredentialsLoginResponseModel loginResponse = given(requestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec)
                .extract().as(WrongCredentialsLoginResponseModel.class);

        String expectedDetailError = "Invalid username or password.";
        String actualDetail = loginResponse.detail();

        assertThat(actualDetail).isEqualTo(expectedDetailError);
    }

    @Test
    public void emptyUsernameLoginTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        given(requestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successRegistrationResponseSpec)
                .extract()
                .as(RegistrationResponseModel.class);

        EmptyUsernameLoginRequestModel emptyUsernameLoginData = new EmptyUsernameLoginRequestModel(password);

        EmptyUsernameLoginResponseModel loginResponse = given(requestSpec)
                .body(emptyUsernameLoginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyUsernameLoginResponseSpec)
                .extract().as(EmptyUsernameLoginResponseModel.class);

        String expectedUsernameError = "This field is required.";
        String actualUsername = loginResponse.username().get(0);

        assertThat(actualUsername).isEqualTo(expectedUsernameError);
    }

    @Test
    public void emptyPasswordLoginTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        given(requestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successRegistrationResponseSpec)
                .extract()
                .as(RegistrationResponseModel.class);

        EmptyPasswordLoginRequestModel emptyPasswordLoginData = new EmptyPasswordLoginRequestModel(username);

        EmptyPasswordLoginResponseModel loginResponse = given(requestSpec)
                .body(emptyPasswordLoginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyPasswordLoginResponseSpec)
                .extract().as(EmptyPasswordLoginResponseModel.class);

        String expectedPasswordError = "This field is required.";
        String actualPassword = loginResponse.password().get(0);;

        assertThat(actualPassword).isEqualTo(expectedPasswordError);
    }
}

package tests;

import models.login.*;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static data.TestData.*;
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
        username = generateUsername();
        password = generatePassword();
        wrongPassword = generateWrongPassword();
    }

    @Test
    @DisplayName("Успешная авторизация")
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


        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();

        assertThat(actualAccess).startsWith(EXPECTED_TOKEN_PATH);
        assertThat(actualRefresh).startsWith(EXPECTED_TOKEN_PATH);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    @DisplayName("Ввод невалидного пароля")
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

        String actualDetail = loginResponse.detail();

        assertThat(actualDetail).isEqualTo(EXPECTED_LOGIN_ERROR_DETAIL);
    }

    @Test
    @DisplayName("Отправка пустого username")
    public void emptyUsernameLoginTest() {

        EmptyUsernameLoginRequestModel emptyUsernameLoginData = new EmptyUsernameLoginRequestModel(password);

        EmptyUsernameLoginResponseModel loginResponse = given(requestSpec)
                .body(emptyUsernameLoginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyUsernameLoginResponseSpec)
                .extract().as(EmptyUsernameLoginResponseModel.class);

        String actualUsername = loginResponse.username().get(0);

        assertThat(actualUsername).isEqualTo(EXPECTED_USERNAME_ERROR);
    }

    @Test
    @DisplayName("Отправка пустого password")
    public void emptyPasswordLoginTest() {

        EmptyPasswordLoginRequestModel emptyPasswordLoginData = new EmptyPasswordLoginRequestModel(username);

        EmptyPasswordLoginResponseModel loginResponse = given(requestSpec)
                .body(emptyPasswordLoginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyPasswordLoginResponseSpec)
                .extract().as(EmptyPasswordLoginResponseModel.class);

        String actualPassword = loginResponse.password().get(0);

        assertThat(actualPassword).isEqualTo(EXPECTED_PASSWORD_ERROR);
    }
}

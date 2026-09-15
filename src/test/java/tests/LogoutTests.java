package tests;

import models.login.LoginRequestModel;
import models.logout.LogoutRequestModel;
import models.logout.WrongTokenLogoutResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static data.TestData.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.BaseSpec.requestSpec;
import static specs.login.LoginSpec.successLoginResponseSpec;
import static specs.logout.LogoutSpec.successfulLogoutResponseSpec;
import static specs.logout.LogoutSpec.wrongTokenLogoutResponseSpec;
import static specs.registration.RegistrationSpec.successRegistrationResponseSpec;

public class LogoutTests extends TestBase {
    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        username = generateUsername();
        password = generatePassword();
    }

    @Test
    @DisplayName("Успешный Logout")
    public void successfulLogoutTest() {

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

        String refreshToken = given(requestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successLoginResponseSpec)
                .extract().path("refresh");

        LogoutRequestModel logoutRequestModel = new LogoutRequestModel(refreshToken);

        given(requestSpec)
                .body(logoutRequestModel)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);

        WrongTokenLogoutResponseModel logoutResponse = given(requestSpec)
                .body(logoutRequestModel)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(wrongTokenLogoutResponseSpec)
                .extract()
                .as(WrongTokenLogoutResponseModel.class);

        String actualDetail = logoutResponse.detail();
        String actualCode = logoutResponse.code();

        assertThat(actualDetail).isEqualTo(EXPECTED_BLOCKED_TOKEN_DETAIL);
        assertThat(actualCode).isEqualTo(EXPECTED_TOKEN_ERROR_CODE);

    }

    @Test
    @DisplayName("Невалидный токен при Logout")
    public void wrongTokenLogoutTest() {

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

        String accessToken = given(requestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successLoginResponseSpec)
                .extract().path("access");


        LogoutRequestModel logoutRequestModel = new LogoutRequestModel(accessToken);

        WrongTokenLogoutResponseModel logoutResponse = given(requestSpec)
                .body(logoutRequestModel)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(wrongTokenLogoutResponseSpec)
                .extract()
                .as(WrongTokenLogoutResponseModel.class);

        String actualDetail = logoutResponse.detail();
        String actualCode = logoutResponse.code();

        assertThat(actualDetail).isEqualTo(EXPECTED_WRONG_TOKEN_DETAIL);
        assertThat(actualCode).isEqualTo(EXPECTED_TOKEN_ERROR_CODE);

    }
}

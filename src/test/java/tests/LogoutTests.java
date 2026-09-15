package tests;

import models.logout.LogoutRequestModel;
import models.logout.WrongTokenLogoutResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.AuthSteps;
import steps.RegistrationSteps;

import static data.TestData.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.BaseSpec.requestSpec;
import static specs.logout.LogoutSpec.successfulLogoutResponseSpec;
import static specs.logout.LogoutSpec.wrongTokenLogoutResponseSpec;

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

        RegistrationSteps registrationSteps = new RegistrationSteps();

        registrationSteps.registerUser(username, password);

        AuthSteps authSteps = new AuthSteps();
        String refreshToken = authSteps.login(username, password).refresh();

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

        RegistrationSteps registrationSteps = new RegistrationSteps();

        registrationSteps.registerUser(username, password);

        AuthSteps authSteps = new AuthSteps();
        String accessToken = authSteps.login(username, password).access();

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

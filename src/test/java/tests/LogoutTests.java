package tests;

import models.logout.LogoutRequestModel;
import models.logout.WrongTokenLogoutResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.AuthSteps;
import steps.RegistrationSteps;

import static data.TestData.*;
import static io.qameta.allure.Allure.step;
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
    @DisplayName("Logout отзывает refresh-токен и отклоняет его повторное использование")
    public void successfulLogoutTest() {

        step("Зарегистрировать пользователя", () -> {
            RegistrationSteps registrationSteps = new RegistrationSteps();
            registrationSteps.registerUser(username, password);
        });

        String refreshToken = step("Войти и получить refresh-токен", () -> {
            AuthSteps authSteps = new AuthSteps();
            return authSteps.login(username, password).refresh();
        });

        LogoutRequestModel logoutRequestModel = new LogoutRequestModel(refreshToken);


        step("Выполнить logout с refresh-токеном и проверить 200", () -> {
            given(requestSpec)
                    .body(logoutRequestModel)
                    .when()
                    .post("/auth/logout/")
                    .then()
                    .spec(successfulLogoutResponseSpec);
        });

        step("Повторить logout с тем же токеном и проверить ошибку блокировки", () -> {
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
        });

    }

    @Test
    @DisplayName("Logout с access-токеном вместо refresh возвращает 401")
    public void wrongTokenLogoutTest() {

        step("Зарегистрировать пользователя", () -> {
            RegistrationSteps registrationSteps = new RegistrationSteps();
            registrationSteps.registerUser(username, password);
        });

        String accessToken = step("Войти и получить access-токен", () -> {
            AuthSteps authSteps = new AuthSteps();
            return authSteps.login(username, password).access();
        });

        LogoutRequestModel logoutRequestModel = new LogoutRequestModel(accessToken);

        step("Отправить access-токен вместо refresh и проверить ошибку 401", () -> {
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
        });

    }
}

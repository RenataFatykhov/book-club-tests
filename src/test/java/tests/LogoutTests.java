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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

        RegistrationSteps registrationSteps = new RegistrationSteps();
        registrationSteps.registerUser(username, password);

        AuthSteps authSteps = new AuthSteps();
        String refreshToken = authSteps.login(username, password).refresh();

        LogoutRequestModel body = new LogoutRequestModel(refreshToken);

        logoutClient.logout(body);

        WrongTokenLogoutResponseModel response = logoutClient.wrongTokenLogout(body);

        step("Проверить сообщение и код ошибки блокировки refresh-токена", () -> {
            String actualDetail = response.detail();
            String actualCode = response.code();

            assertThat(actualDetail).isEqualTo(EXPECTED_BLOCKED_TOKEN_DETAIL);
            assertThat(actualCode).isEqualTo(EXPECTED_TOKEN_ERROR_CODE);
        });

    }

    @Test
    @DisplayName("Logout с access-токеном вместо refresh возвращает 401")
    public void wrongTokenLogoutTest() {

        RegistrationSteps registrationSteps = new RegistrationSteps();
        registrationSteps.registerUser(username, password);

        AuthSteps authSteps = new AuthSteps();
        String accessToken = authSteps.login(username, password).access();

        LogoutRequestModel body = new LogoutRequestModel(accessToken);

        WrongTokenLogoutResponseModel response = logoutClient.wrongTokenLogout(body);

        step("Проверить сообщение и код ошибки при передаче access-токена вместо refresh", () -> {
            String actualDetail = response.detail();
            String actualCode = response.code();

            assertThat(actualDetail).isEqualTo(EXPECTED_WRONG_TOKEN_DETAIL);
            assertThat(actualCode).isEqualTo(EXPECTED_TOKEN_ERROR_CODE);
        });

    }
}

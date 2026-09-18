package tests;

import models.login.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.RegistrationSteps;

import static data.TestData.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

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
    @DisplayName("Вход с верными учётными данными возвращает access- и refresh-токены")
    public void successfulLoginTest() {

        RegistrationSteps registrationSteps = new RegistrationSteps();
        registrationSteps.registerUser(username, password);

        LoginRequestModel body = new LoginRequestModel(username, password);

        SuccessfulLoginResponseModel response = loginClient.login(body);

        step("Проверить префикс access- и refresh-токенов и различие их значений", () -> {
            String actualAccess = response.access();
            String actualRefresh = response.refresh();

            assertThat(actualAccess).startsWith(EXPECTED_TOKEN_PATH);
            assertThat(actualRefresh).startsWith(EXPECTED_TOKEN_PATH);
            assertThat(actualAccess).isNotEqualTo(actualRefresh);
        });
    }

    @Test
    @DisplayName("Вход с неверным паролем возвращает 401")
    public void wrongCredentialsLoginTest() {

        RegistrationSteps registrationSteps = new RegistrationSteps();
        registrationSteps.registerUser(username, password);

        LoginRequestModel body = new LoginRequestModel(username, wrongPassword);

        WrongCredentialsLoginResponseModel response = loginClient.wrongCredentialsLogin(body);

        step("Проверить сообщение об ошибке входа с неверным паролем", () -> {
            String actualDetail = response.detail();

            assertThat(actualDetail).isEqualTo(EXPECTED_LOGIN_ERROR_DETAIL);
        });
    }

    @Test
    @DisplayName("Вход без поля username возвращает 400")
    public void emptyUsernameLoginTest() {

        EmptyUsernameLoginRequestModel emptyUsernameLoginData = new EmptyUsernameLoginRequestModel(password);

        EmptyUsernameLoginResponseModel response = loginClient.emptyUsernameLogin(emptyUsernameLoginData);

        step("Проверить сообщение об обязательности поля username", () -> {
            String actualUsername = response.username().get(0);

            assertThat(actualUsername).isEqualTo(EXPECTED_USERNAME_ERROR);
        });

    }

    @Test
    @DisplayName("Вход без поля password возвращает 400")
    public void emptyPasswordLoginTest() {

        EmptyPasswordLoginRequestModel emptyPasswordLoginData = new EmptyPasswordLoginRequestModel(username);

        EmptyPasswordLoginResponseModel response = loginClient.emptyPasswordLogin(emptyPasswordLoginData);

        step("Проверить сообщение об обязательности поля password", () -> {
            String actualPassword = response.password().get(0);

            assertThat(actualPassword).isEqualTo(EXPECTED_PASSWORD_ERROR);
        });
    }
}

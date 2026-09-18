package tests;

import models.update.user.AuthErrorResponseModel;
import models.update.user.SuccessfulUserUpdateResponseModel;
import models.update.user.UserUpdateRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.AuthSteps;
import steps.RegistrationSteps;

import static data.TestData.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateUserTests extends TestBase {
    String username;
    String newUsername;
    String password;
    String firstName;
    String lastName;
    String email;

    @BeforeEach
    public void prepareTestData() {
        username = generateUsername();
        newUsername = generateNewUsername(username);
        password = generatePassword();
        firstName = generateFirstName();
        lastName = generateLastName();
        email = generateEmail();
    }

    @Test
    @DisplayName("Обновление профиля возвращает переданные значения полей")
    public void successfulPutUpdateUserTest() {

        step("Зарегистрировать пользователя", () -> {
            RegistrationSteps registrationSteps = new RegistrationSteps();
            registrationSteps.registerUser(username, password);
        });

        String actualAccess = step("Войти и получить access-токен", () -> {
            AuthSteps authSteps = new AuthSteps();
            return authSteps.login(username, password).access();
        });

        step("Обновить профиль и проверить значения полей в ответе", () -> {
            assertThat(newUsername).isNotEqualTo(username);

            UserUpdateRequestModel body = new UserUpdateRequestModel(newUsername, firstName, lastName, email);

            SuccessfulUserUpdateResponseModel response = userClient.updateUser(body, actualAccess);

            String expectedUsername = body.username();
            String expectedFirstName = body.firstName();
            String expectedLastName = body.lastName();
            String expectedEmail = body.email();

            String actualUsername = response.username();
            String actualFirstName = response.firstName();
            String actualLastName = response.lastName();
            String actualEmail = response.email();

            assertThat(actualUsername).isEqualTo(expectedUsername);
            assertThat(actualFirstName).isEqualTo(expectedFirstName);
            assertThat(actualLastName).isEqualTo(expectedLastName);
            assertThat(actualEmail).isEqualTo(expectedEmail);
        });

    }

    @Test
    @DisplayName("Обновление профиля без Authorization возвращает 401")
    public void authErrorPutUpdateUserTest() {

        UserUpdateRequestModel body = new UserUpdateRequestModel(newUsername, firstName, lastName, email);

        AuthErrorResponseModel response = userClient.authErrorUpdateUser(body);

        String actualDetail = response.detail();

        assertThat(actualDetail).isEqualTo(EXPECTED_UPDATE_AUTH_DETAIL);

    }


}

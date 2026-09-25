package tests.ui;

import models.registration.RegistrationBodyModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.RegistrationPage;

import static data.TestData.generatePassword;
import static data.TestData.generateUsername;

public class RegistrationUiTests extends UiTestBase {

    RegistrationPage registrationPage = new RegistrationPage();

    String username;
    String password;
    String wrongPassword;

    @BeforeEach
    public void prepareTestData() {
        username = generateUsername();
        password = generatePassword();
        wrongPassword = generatePassword();
    }


    @Test
    @DisplayName("После успешной регистрации пользователя происходит автоматическая авторизация: отображаются вкладки для авторизованного пользователя")
    public void successfulRegistrationTest() {

        registrationPage
                .openRegistrationPage()
                .setUsername(username)
                .setPassword(password)
                .setTheSamePasswordAndConfirm(password)
                .checkMainPageCondition()
                .checkMainPageHaveProfileButton()
                .checkMainPageHaveClubButton()
                .checkMainPageHaveCreateClubButton();

    }

    @Test
    @DisplayName("Ввод некорректного пароля в подтверждении пароля отображает ошибку")
    public void notTheSamePasswordInRegistrationTest() {

        registrationPage
                .openRegistrationPage()
                .setUsername(username)
                .setPassword(password)
                .setNotTheSamePasswordAndConfirm(wrongPassword)
                .checkMismatchPasswordError();
    }

    @Test
    @DisplayName("Повторная регистрация на уже созданного пользователя отображает ошибку")
    public void existingUserRegistrationUiTests() {

        RegistrationBodyModel regBody = new RegistrationBodyModel(username, password);
        registrationClient.register(regBody);

        registrationPage
                .openRegistrationPage()
                .setUsername(regBody.username())
                .setPassword(regBody.password())
                .setTheSamePasswordAndConfirm(regBody.password())
                .checkSameCredentialsError();


    }

}

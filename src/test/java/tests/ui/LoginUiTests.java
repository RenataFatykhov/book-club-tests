package tests.ui;

import models.registration.RegistrationBodyModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.LoginPage;

import static data.TestData.generatePassword;
import static data.TestData.generateUsername;

public class LoginUiTests extends UiTestBase {

    LoginPage loginPage = new LoginPage();

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
    @DisplayName("После успешной авторизации нового пользователя: отображаются вкладки для авторизованного пользователя")
    public void successfulLoginTest() {

        RegistrationBodyModel regBody = new RegistrationBodyModel(username, password);
        registrationClient.register(regBody);

        loginPage
                .openLoginPage()
                .setUsername(username)
                .setPassword(password)
                .checkMainPageCondition()
                .checkMainPageHaveProfileButton()
                .checkMainPageHaveClubButton()
                .checkMainPageHaveCreateClubButton();

    }

    @Test
    @DisplayName("После ввода некорректного пароля отображается ошибка авторизации")
    public void wrongCredentialsLoginTest() {

        RegistrationBodyModel regBody = new RegistrationBodyModel(username, password);
        registrationClient.register(regBody);

        loginPage
                .openLoginPage()
                .setUsername(username)
                .setPassword(wrongPassword)
                .checkWrongCredentialsError();

    }
}

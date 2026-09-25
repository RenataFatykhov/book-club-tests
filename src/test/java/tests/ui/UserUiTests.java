package tests.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.local_storage.AuthModel;
import models.local_storage.UserLocalStorageModel;
import models.login.LoginRequestModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static data.TestData.generatePassword;
import static data.TestData.generateUsername;
import static io.qameta.allure.Allure.step;

public class UserUiTests extends UiTestBase {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        username = generateUsername();
        password = generatePassword();
    }

    @Test
    @DisplayName("Регистрация нового пользователя после логина отображает корректное инфо в профиле нового пользователя")
    public void registrationUiTests() {

        RegistrationBodyModel regBody = new RegistrationBodyModel(username, password);
        RegistrationResponseModel regResponse = registrationClient.register(regBody);

        LoginRequestModel loginBody = new LoginRequestModel(username, password);
        SuccessfulLoginResponseModel loginResponse = loginClient.login(loginBody);

        String accessToken = loginResponse.access();
        String refreshToken = loginResponse.refresh();

        String localStorageAuthBody = step("Создание JSON авторизации для localStorage", () -> {
            UserLocalStorageModel user = new UserLocalStorageModel(
                    regResponse.id(),
                    regResponse.username(),
                    regResponse.firstName(),
                    regResponse.lastName(),
                    regResponse.email(),
                    regResponse.remoteAddr()
            );
            AuthModel auth = new AuthModel(user, accessToken, refreshToken, true);
            return new ObjectMapper().writeValueAsString(auth);
        });

        String expectedAvatarText = username.substring(0, 1).toUpperCase(Locale.ROOT);

        open("/favicon.ico");
        localStorage().setItem("book_club_auth", localStorageAuthBody);


        open("/profile");
        $("[data-testid=profile-link]").shouldHave(text("Профиль")).click();
        $(".profile-info").shouldBe(visible);
        $(".avatar").shouldHave(text(expectedAvatarText));
        $(".info-grid").parent().shouldHave(text(username));

    }
}

package tests.ui;

import models.registration.RegistrationBodyModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.TestData.generatePassword;
import static data.TestData.generateUsername;

public class RegistrationUiTests extends TestBase {

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

        open("/signup");
        $("[data-testid=username-input]").setValue(username);
        $("[data-testid=password-input]").setValue(password);
        $("[data-testid=confirm-password-input]").setValue(password).pressEnter();

        $(".clubs-page").shouldBe(visible);
        $("[data-testid=profile-link]").shouldHave(text("Профиль"));
        $("[data-testid=clubs-link]").shouldHave(text("Клубы"));
        $("[data-testid=create-club-link]").shouldHave(text("Создать клуб"));

    }

    @Test
    @DisplayName("Ввод некорректного пароля в подтверждении пароля отображает ошибку")
    public void notTheSamePasswordInRegistrationTest() {

        open("/signup");
        $("[data-testid=username-input]").setValue(username);
        $("[data-testid=password-input]").setValue(password);
        $("[data-testid=confirm-password-input]").setValue(wrongPassword).pressEnter();
        $("[data-testid=password-mismatch-error]").shouldBe(visible).shouldHave(text("Пароли не совпадают"));
    }

    @Test
    @DisplayName("Повторная регистрация на уже созданного пользователя отображает ошибку")
    public void existingUserRegistrationUiTests() {

        RegistrationBodyModel regBody = new RegistrationBodyModel(username, password);
        registrationClient.register(regBody);

        open("/signup");
        $("[data-testid=username-input]").setValue(regBody.username());
        $("[data-testid=password-input]").setValue(regBody.password());
        $("[data-testid=confirm-password-input]").setValue(regBody.password()).pressEnter();
        $("[data-testid=error-message]").shouldBe(visible).shouldHave(text("Ошибка при регистрации"));
    }

}

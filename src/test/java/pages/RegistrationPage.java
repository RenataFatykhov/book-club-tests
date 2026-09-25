package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.TestData.*;

public class RegistrationPage {
    private final SelenideElement usernameInput = $("[data-testid=username-input]");
    private final SelenideElement passwordInput = $("[data-testid=password-input]");
    private final SelenideElement confirmUsernameInput = $("[data-testid=confirm-password-input]");
    private final SelenideElement mainPage = $(".clubs-page");
    private final SelenideElement profileBtn = $("[data-testid=profile-link]");
    private final SelenideElement clubBtn = $("[data-testid=clubs-link]");
    private final SelenideElement createClubBtn = $("[data-testid=create-club-link]");
    private final SelenideElement missmatchPasswordError = $("[data-testid=password-mismatch-error]");
    private final SelenideElement sameCredentialsError = $("[data-testid=password-mismatch-error]");


    @Step("Открыть страницу регистрации")
    public RegistrationPage openRegistrationPage() {
        open("/signup");
        return this;
    }

    @Step("Заполнить поле username")
    public RegistrationPage setUsername(String value) {
        usernameInput.setValue(value);
        return this;
    }

    @Step("Заполнить поле password")
    public RegistrationPage setPassword(String value) {
        passwordInput.setValue(value);
        return this;
    }

    @Step("Заполнить поле confirm password верным паролем")
    public RegistrationPage setTheSamePasswordAndConfirm(String value) {
        confirmUsernameInput.setValue(value).pressEnter();
        return this;
    }

    @Step("Заполнить поле confirm password неверным паролем")
    public RegistrationPage setNotTheSamePasswordAndConfirm(String value) {
        confirmUsernameInput.setValue(value).pressEnter();
        return this;
    }

    @Step("Отображается главная страница")
    public RegistrationPage checkMainPageCondition() {
        mainPage.shouldBe(visible);
        return this;
    }

    @Step("На главной странице отображается кнопка Профиль")
    public RegistrationPage checkMainPageHaveProfileButton() {
        profileBtn.shouldHave(text(EXPECTED_PROFILE_BTN_NAME));
        return this;
    }

    @Step("На главной странице отображается кнопка Клубы")
    public RegistrationPage checkMainPageHaveClubButton() {
        clubBtn.shouldHave(text(EXPECTED_CLUB_BTN_NAME));
        return this;
    }

    @Step("На главной странице отображается кнопка Создать клуб")
    public RegistrationPage checkMainPageHaveCreateClubButton() {
        createClubBtn.shouldHave(text(EXPECTED_CREATE_CLUB_BTN_NAME));
        return this;
    }

    @Step("Отображается ошибка несовпадения пароля подтверждения")
    public RegistrationPage checkMismatchPasswordError() {
        missmatchPasswordError.shouldBe(visible).shouldHave(text(EXPECTED_MISMATCH_PASSWORD_ERROR_MESSAGE));
        return this;
    }

    @Step("Отображается ошибка несовпадения пароля подтверждения")
    public RegistrationPage checkSameCredentialsError() {
        sameCredentialsError.shouldBe(visible).shouldHave(text(EXPECTED_SAME_CREDENTIALS_ERROR_MESSAGE));
        return this;
    }


}

package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.local_storage.AuthModel;
import models.local_storage.UserModel;
import models.login.LoginRequestModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static data.TestData.generatePassword;
import static data.TestData.generateUsername;

public class ClubTests extends TestBase {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        username = generateUsername();
        password = generatePassword();
    }

    @Test
    public void cantLeaveClubAsAdminTest() throws JsonProcessingException {
        // register user
        RegistrationBodyModel regBody = new RegistrationBodyModel(username, password);
        RegistrationResponseModel regResponse = registrationClient.register(regBody);

        //login user
        LoginRequestModel loginBody = new LoginRequestModel(username, password);
        SuccessfulLoginResponseModel loginResponse = loginClient.login(loginBody);

        String accessToken = loginResponse.access();
        String refreshToken = loginResponse.refresh();

        UserModel user = new UserModel(
                regResponse.id(),
                regResponse.username(),
                regResponse.firstName(),
                regResponse.lastName(),
                regResponse.email(),
                regResponse.remoteAddr()
        );
        AuthModel auth = new AuthModel(user, accessToken, refreshToken, true);
        String localStorageAuthBody = new ObjectMapper().writeValueAsString(auth);

        open("/favicon.ico");
        localStorage().setItem("book_club_auth", localStorageAuthBody);

        // create club
        open("/clubs/create");
        $("#bookTitle").setValue(username);
        $("#bookAuthors").setValue(username);
        $("#publicationYear").setValue("2020");
        $("#description").setValue(username);
        $("#telegramChatLink").setValue("https://t.me/qa_guru" + username).pressEnter();

        // open club
        $(".filter-options").$(byText("Мои клубы")).click();
        $(".clubs-list")
                .$$(".club-card h2")
                .findBy(exactText(username))
                .closest(".club-card")
                .$(".open-btn")
                .click();

        // wrong leave club
        $(".club-content").shouldBe(visible);
        $(".leave-btn").click();
        confirm();
        $(".error").shouldHave(text("Не удалось покинуть клуб"));
    }
}

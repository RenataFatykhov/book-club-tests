package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.clubs.CreateClubRequestModel;
import models.clubs.ResultsClubModel;
import models.local_storage.AuthModel;
import models.local_storage.UserLocalStorageModel;
import models.login.LoginRequestModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static data.TestData.*;
import static io.qameta.allure.Allure.step;

public class ClubTests extends TestBase {

    String bookTitle;
    String bookAuthors;
    int publicationYear;
    String description;
    String telegramChatLink;
    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        username = generateUsername();
        password = generatePassword();
        bookTitle = generateBookTitle();
        bookAuthors = generateBookAuthors();
        publicationYear = generatePublicationYear();
        description = generateDescription();
        telegramChatLink = generateTelegramChatLink();
    }

    @Test
    public void cantLeaveClubAsOwnerTest() {

        step("Открытие приложения с авторизацией из localStorage", () -> {
            // register user
            RegistrationBodyModel regBody = new RegistrationBodyModel(username, password);
            RegistrationResponseModel regResponse = registrationClient.register(regBody);

            //login user
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

            // create club
            CreateClubRequestModel body = new CreateClubRequestModel(
                    bookTitle,
                    bookAuthors,
                    publicationYear,
                    description,
                    telegramChatLink
            );

            ResultsClubModel creatClub = createClubsClient.createClub(body, accessToken);
            int clubId = creatClub.id();

            // open club
            open("/favicon.ico");
            localStorage().setItem("book_club_auth", localStorageAuthBody);
            open("/clubs/" + clubId);

        });

        step("[UI] Проверка отображения ошибки о невозможности покинуть клуб создателем", () -> {
            // can't leave club
            $(".club-content").shouldBe(visible);
            $(".leave-btn").click();
            confirm();
            $(".error").shouldHave(text("Не удалось покинуть клуб"));
        });
    }
}

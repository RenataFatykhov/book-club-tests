package tests;

import models.clubs.CreateClubRequestModel;
import models.clubs.ExistingClubResponseModel;
import models.clubs.ResultsClubModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.AuthSteps;
import steps.RegistrationSteps;

import static data.TestData.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateClubsTests extends TestBase {

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
    @DisplayName("Создание нового книжного клуба возвращает 201")
    public void successfulCreateClubTest() {

        RegistrationSteps registrationSteps = new RegistrationSteps();
        int expectedId = registrationSteps.registerUser(username, password).id();

        AuthSteps authSteps = new AuthSteps();
        String accessToken = authSteps.login(username, password).access();

        CreateClubRequestModel body = new CreateClubRequestModel(
                bookTitle,
                bookAuthors,
                publicationYear,
                description,
                telegramChatLink
        );

        ResultsClubModel response = createClubsClient.createClub(body, accessToken);

        step("Проверить данные созданного книжного клуба", () -> {
            assertThat(response.owner()).isEqualTo(expectedId);
            assertThat(response.bookTitle()).isEqualTo(body.bookTitle());
            assertThat(response.bookAuthors()).isEqualTo(body.bookAuthors());
            assertThat(response.publicationYear()).isEqualTo(body.publicationYear());
            assertThat(response.description()).isEqualTo(body.description());
            assertThat(response.telegramChatLink()).isEqualTo(body.telegramChatLink());
        });
    }

    @Test
    @DisplayName("Повторное создание книжного клуба возвращает 400")
    public void createExistingClubTest() {

        RegistrationSteps registrationSteps = new RegistrationSteps();
        registrationSteps.registerUser(username, password);

        AuthSteps authSteps = new AuthSteps();
        String accessToken = authSteps.login(username, password).access();

        CreateClubRequestModel body = new CreateClubRequestModel(
                bookTitle,
                bookAuthors,
                publicationYear,
                description,
                telegramChatLink
        );

        createClubsClient.createClub(body, accessToken);

        ExistingClubResponseModel secondResponse = createClubsClient.createExistingClub(body, accessToken);

        step("Проверить сообщение о невозможности повторно создать уже существующий книжный клуб", () -> {
            assertThat(secondResponse.bookTitle().get(0)).isEqualTo(EXPECTED_EXISTING_CLUB_ERROR_MESSAGE);
        });
    }
}

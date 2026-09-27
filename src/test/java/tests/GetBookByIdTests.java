package tests;

import models.clubs.CreateClubRequestModel;
import models.clubs.ResultsClubModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.AuthSteps;
import steps.RegistrationSteps;

import static data.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GetBookByIdTests extends TestBase {

    @Test
    @DisplayName("Получение существующего книжного клуба по id возвращает 200")
    public void getBookByIdTest() {
        String username = generateUsername();
        String password = generatePassword();
        RegistrationSteps registrationSteps = new RegistrationSteps();
        registrationSteps.registerUser(username, password);

        String accessToken = new AuthSteps().login(username, password).access();
        CreateClubRequestModel body = new CreateClubRequestModel(
                generateBookTitle(),
                generateBookAuthors(),
                generatePublicationYear(),
                generateDescription(),
                generateTelegramChatLink()
        );
        ResultsClubModel createdClub = createClubsClient.createClub(body, accessToken);

        ResultsClubModel response = getByIdBookClient.getBookById(createdClub.id());

        assertThat(response.id()).isEqualTo(createdClub.id());
        assertThat(response.bookTitle()).isEqualTo(body.bookTitle());
        assertThat(response.bookAuthors()).isEqualTo(body.bookAuthors());
    }
}

package tests;

import models.clubs.CreateClubRequestModel;
import models.clubs.ResultsClubModel;
import models.clubs.review.CreateReviewRequestModel;
import models.clubs.review.ReviewModel;
import models.clubs.review.ReviewErrorResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.AuthSteps;
import steps.RegistrationSteps;

import static data.TestData.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateReviewsTests extends TestBase {

    String username;
    String password;
    int club;
    String review;
    Integer assessment;
    Integer readPages;
    String bookTitle;
    String bookAuthors;
    int publicationYear;
    String description;
    String telegramChatLink;

    @BeforeEach
    public void prepareTestData() {
        username = generateUsername();
        password = generatePassword();
        review = generateReview();
        assessment = generateAssessment();
        bookTitle = generateBookTitle();
        bookAuthors = generateBookAuthors();
        publicationYear = generatePublicationYear();
        description = generateDescription();
        telegramChatLink = generateTelegramChatLink();
        readPages = generateReadPages();
    }

    @Test
    @DisplayName("Создание отзыва на книгу через получение списка книг")
    public void successfulCreateReviewTest() {

        RegistrationSteps registrationSteps = new RegistrationSteps();
        int id = registrationSteps.registerUser(username, password).id();

        AuthSteps authSteps = new AuthSteps();
        String accessToken = authSteps.login(username, password).access();

        CreateClubRequestModel createBody = new CreateClubRequestModel(
                bookTitle,
                bookAuthors,
                publicationYear,
                description,
                telegramChatLink
        );

        ResultsClubModel createBookResponse = createClubsClient.createClub(createBody, accessToken);

        club = createBookResponse.id();

        CreateReviewRequestModel body = new CreateReviewRequestModel(
                club,
                review,
                assessment,
                readPages
        );

        ReviewModel createReviewResponse = createReviewsClient.createReview(body, accessToken);

        step("Проверить данные созданного отзыва", () -> {
            assertThat(createReviewResponse.user().id()).isEqualTo(id);
            assertThat(createReviewResponse.user().username()).isEqualTo(username);
            assertThat(createReviewResponse.club()).isEqualTo(club);
            assertThat(createReviewResponse.assessment()).isEqualTo(assessment);
            assertThat(createReviewResponse.readPages()).isEqualTo(readPages);
            assertThat(createReviewResponse.review()).isEqualTo(review);
        });
    }

    @Test
    @DisplayName("Создание отзыва без Authorization возвращает 401")
    public void unauthorizedCreateReviewTest() {
        CreateReviewRequestModel body = new CreateReviewRequestModel(
                1,
                review,
                assessment,
                readPages
        );

        ReviewErrorResponseModel response = createReviewsClient.createReviewWithoutAuthorization(body);

        step("Проверить сообщение об отсутствии авторизации", () ->
                assertThat(response.detail()).isEqualTo(EXPECTED_NOT_AUTH_DETAIL));
    }

}

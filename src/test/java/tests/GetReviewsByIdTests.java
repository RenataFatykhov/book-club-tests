package tests;

import models.clubs.CreateClubRequestModel;
import models.clubs.ResultsClubModel;
import models.clubs.review.CreateReviewRequestModel;
import models.clubs.review.ReviewErrorResponseModel;
import models.clubs.review.ReviewModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.AuthSteps;
import steps.RegistrationSteps;

import static data.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GetReviewsByIdTests extends TestBase {

    @Test
    @DisplayName("Получение существующего отзыва по id возвращает 200")
    public void getReviewByIdTest() {
        ReviewModel createdReview = createReview();

        ReviewModel response = getByIdReviewClient.getReviewById(createdReview.id());

        assertThat(response.id()).isEqualTo(createdReview.id());
        assertThat(response.club()).isEqualTo(createdReview.club());
        assertThat(response.review()).isEqualTo(createdReview.review());
    }

    @Test
    @DisplayName("Получение несуществующего отзыва по id возвращает 404")
    public void getMissingReviewByIdTest() {
        ReviewErrorResponseModel response = getByIdReviewClient.getMissingReviewById(Integer.MAX_VALUE);

        assertThat(response.detail()).isEqualTo(EXPECTED_REVIEW_NOT_FOUND_ERROR_MESSAGE);
    }

    private ReviewModel createReview() {
        String username = generateUsername();
        String password = generatePassword();
        RegistrationSteps registrationSteps = new RegistrationSteps();
        registrationSteps.registerUser(username, password);

        String accessToken = new AuthSteps().login(username, password).access();
        CreateClubRequestModel createClubBody = new CreateClubRequestModel(
                generateBookTitle(),
                generateBookAuthors(),
                generatePublicationYear(),
                generateDescription(),
                generateTelegramChatLink()
        );
        ResultsClubModel club = createClubsClient.createClub(createClubBody, accessToken);
        CreateReviewRequestModel createReviewBody = new CreateReviewRequestModel(
                club.id(),
                generateReview(),
                generateAssessment(),
                generateReadPages()
        );

        return createReviewsClient.createReview(createReviewBody, accessToken);
    }
}

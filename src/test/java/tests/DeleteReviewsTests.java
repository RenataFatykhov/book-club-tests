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

public class DeleteReviewsTests extends TestBase {

    @Test
    @DisplayName("Удаление собственного отзыва возвращает 204")
    public void successfulDeleteReviewTest() {
        ReviewContext context = createOwnedReview();

        getByIdReviewClient.deleteReview(context.reviewId(), context.accessToken());

        ReviewErrorResponseModel response = getByIdReviewClient.getMissingReviewById(context.reviewId());
        assertThat(response.detail()).isEqualTo(EXPECTED_REVIEW_NOT_FOUND_ERROR_MESSAGE);
    }

    @Test
    @DisplayName("Удаление отзыва без Authorization возвращает 401")
    public void unauthorizedDeleteReviewTest() {
        ReviewErrorResponseModel response = getByIdReviewClient.deleteReviewWithoutAuthorization(1);

        assertThat(response.detail()).isEqualTo(EXPECTED_NOT_AUTH_DETAIL);
    }

    private ReviewContext createOwnedReview() {
        String username = generateUsername();
        String password = generatePassword();
        RegistrationSteps registrationSteps = new RegistrationSteps();
        registrationSteps.registerUser(username, password);

        String accessToken = new AuthSteps().login(username, password).access();
        ResultsClubModel club = createClubsClient.createClub(new CreateClubRequestModel(
                generateBookTitle(),
                generateBookAuthors(),
                generatePublicationYear(),
                generateDescription(),
                generateTelegramChatLink()
        ), accessToken);
        ReviewModel review = createReviewsClient.createReview(new CreateReviewRequestModel(
                club.id(),
                generateReview(),
                generateAssessment(),
                generateReadPages()
        ), accessToken);

        return new ReviewContext(accessToken, review.id());
    }

    private record ReviewContext(String accessToken, int reviewId) {
    }
}

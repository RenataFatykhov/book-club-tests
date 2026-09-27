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

public class PutReviewsTests extends TestBase {

    @Test
    @DisplayName("Полное обновление собственного отзыва возвращает 200")
    public void successfulPutReviewTest() {
        ReviewContext context = createOwnedReview();
        CreateReviewRequestModel body = new CreateReviewRequestModel(
                context.clubId(),
                generateReview(),
                generateAssessment(),
                generateReadPages()
        );

        ReviewModel response = getByIdReviewClient.updateReview(context.reviewId(), body, context.accessToken());

        assertThat(response.club()).isEqualTo(body.club());
        assertThat(response.review()).isEqualTo(body.review());
        assertThat(response.assessment()).isEqualTo(body.assessment());
        assertThat(response.readPages()).isEqualTo(body.readPages());
        assertThat(response.modified()).isNotNull();
    }

    @Test
    @DisplayName("Полное обновление отзыва без Authorization возвращает 401")
    public void unauthorizedPutReviewTest() {
        CreateReviewRequestModel body = new CreateReviewRequestModel(
                1,
                generateReview(),
                generateAssessment(),
                generateReadPages()
        );

        ReviewErrorResponseModel response = getByIdReviewClient.updateReviewWithoutAuthorization(1, body);

        assertThat(response.detail()).isEqualTo(EXPECTED_NOT_AUTH_DETAIL);
    }

    @Test
    @DisplayName("Частичное обновление собственного отзыва возвращает 200")
    public void successfulPatchReviewTest() {
        ReviewContext context = createOwnedReview();
        String updatedReview = generateReview();

        ReviewModel response = getByIdReviewClient.patchReview(
                context.reviewId(),
                updatedReview,
                context.accessToken()
        );

        assertThat(response.id()).isEqualTo(context.reviewId());
        assertThat(response.review()).isEqualTo(updatedReview);
        assertThat(response.modified()).isNotNull();
    }

    @Test
    @DisplayName("Частичное обновление отзыва без Authorization возвращает 401")
    public void unauthorizedPatchReviewTest() {
        ReviewErrorResponseModel response = getByIdReviewClient.patchReviewWithoutAuthorization(1, generateReview());

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

        return new ReviewContext(accessToken, club.id(), review.id());
    }

    private record ReviewContext(String accessToken, int clubId, int reviewId) {
    }
}

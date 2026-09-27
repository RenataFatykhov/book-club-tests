package clients;

import io.qameta.allure.Step;
import models.clubs.review.CreateReviewRequestModel;
import models.clubs.review.ReviewModel;
import models.clubs.review.ReviewErrorResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.review.CreateReviewSpec.successfulCreateReviewResponseSpec;
import static specs.review.CreateReviewSpec.unauthorizedReviewResponseSpec;

public class CreateReviewsApiClient {

    @Step("Создать отзыв на книгу")
    public ReviewModel createReview(CreateReviewRequestModel body, String actualAccess) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + actualAccess)
                .body(body)
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(successfulCreateReviewResponseSpec)
                .extract()
                .as(ReviewModel.class);
    }

    @Step("Создать отзыв без авторизации")
    public ReviewErrorResponseModel createReviewWithoutAuthorization(CreateReviewRequestModel body) {
        return given(requestSpec)
                .body(body)
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(unauthorizedReviewResponseSpec)
                .extract()
                .as(ReviewErrorResponseModel.class);
    }
}

package clients;

import io.qameta.allure.Step;
import models.clubs.review.CreateReviewRequestModel;
import models.clubs.review.ReviewModel;
import models.clubs.review.ReviewErrorResponseModel;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.review.CreateReviewSpec.unauthorizedReviewResponseSpec;
import static specs.review.ReviewSpec.getReviewByIdResponseSpec;
import static specs.review.ReviewSpec.notFoundReviewResponseSpec;
import static specs.review.ReviewSpec.successfulDeleteReviewResponseSpec;

public class GetByIdReviewApiClient {

    @Step("Получить отзыв по id")
    public ReviewModel getReviewById(int reviewId) {
        return given(requestSpec)
                .when()
                .get("/clubs/reviews/" + reviewId + "/")
                .then()
                .spec(getReviewByIdResponseSpec)
                .extract()
                .as(ReviewModel.class);
    }

    @Step("Получить несуществующий отзыв")
    public ReviewErrorResponseModel getMissingReviewById(int reviewId) {
        return given(requestSpec)
                .when()
                .get("/clubs/reviews/" + reviewId + "/")
                .then()
                .spec(notFoundReviewResponseSpec)
                .extract()
                .as(ReviewErrorResponseModel.class);
    }

    @Step("Полностью обновить отзыв")
    public ReviewModel updateReview(int reviewId, CreateReviewRequestModel body, String accessToken) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .put("/clubs/reviews/" + reviewId + "/")
                .then()
                .spec(getReviewByIdResponseSpec)
                .extract()
                .as(ReviewModel.class);
    }

    @Step("Полностью обновить отзыв без авторизации")
    public ReviewErrorResponseModel updateReviewWithoutAuthorization(int reviewId, CreateReviewRequestModel body) {
        return given(requestSpec)
                .body(body)
                .when()
                .put("/clubs/reviews/" + reviewId + "/")
                .then()
                .spec(unauthorizedReviewResponseSpec)
                .extract()
                .as(ReviewErrorResponseModel.class);
    }

    @Step("Частично обновить текст отзыва")
    public ReviewModel patchReview(int reviewId, String review, String accessToken) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(Map.of("review", review))
                .when()
                .patch("/clubs/reviews/" + reviewId + "/")
                .then()
                .spec(getReviewByIdResponseSpec)
                .extract()
                .as(ReviewModel.class);
    }

    @Step("Частично обновить отзыв без авторизации")
    public ReviewErrorResponseModel patchReviewWithoutAuthorization(int reviewId, String review) {
        return given(requestSpec)
                .body(Map.of("review", review))
                .when()
                .patch("/clubs/reviews/" + reviewId + "/")
                .then()
                .spec(unauthorizedReviewResponseSpec)
                .extract()
                .as(ReviewErrorResponseModel.class);
    }

    @Step("Удалить отзыв")
    public void deleteReview(int reviewId, String accessToken) {
        given(requestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("/clubs/reviews/" + reviewId + "/")
                .then()
                .spec(successfulDeleteReviewResponseSpec);
    }

    @Step("Удалить отзыв без авторизации")
    public ReviewErrorResponseModel deleteReviewWithoutAuthorization(int reviewId) {
        return given(requestSpec)
                .when()
                .delete("/clubs/reviews/" + reviewId + "/")
                .then()
                .spec(unauthorizedReviewResponseSpec)
                .extract()
                .as(ReviewErrorResponseModel.class);
    }
}

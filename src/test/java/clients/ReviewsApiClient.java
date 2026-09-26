package clients;

import io.qameta.allure.Step;
import models.clubs.ClubRequestPaginationModel;
import models.clubs.review.ReviewResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.review.ReviewSpec.getReviewResponseSpec;

public class ReviewsApiClient {

    @Step("Получить список клубов с отзываит: запрос без параметров")
    public ReviewResponseModel noParametersGetReviews() {
        return given(requestSpec)
                .when()
                .get("/clubs/reviews/")
                .then()
                .spec(getReviewResponseSpec)
                .extract()
                .as(ReviewResponseModel.class);
    }

    @Step("Получить список клубов с отзываими с параметрами пагинации")
    public ReviewResponseModel paginationGetReviews(ClubRequestPaginationModel parameters) {
        return given(requestSpec)
                .queryParam("page", parameters.page())
                .queryParam("page_size", parameters.page_size())
                .when()
                .get("/clubs/reviews/")
                .then()
                .spec(getReviewResponseSpec)
                .extract()
                .as(ReviewResponseModel.class);
    }
}

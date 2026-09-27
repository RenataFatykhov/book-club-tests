package clients;

import io.qameta.allure.Step;
import models.clubs.ResultsClubModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.club.ClubSpec.getClubByIdResponseSpec;

public class GetByIdBookApiClient {

    @Step("Получить инфо о созданном клубе")
    public ResultsClubModel getBookById(int clubId) {
        return given(requestSpec)
                .when()
                .get("/clubs/" + clubId + "/")
                .then()
                .spec(getClubByIdResponseSpec)
                .extract()
                .as(ResultsClubModel.class);
    }
}

package clients;

import io.qameta.allure.Step;
import models.clubs.CreateClubRequestModel;
import models.clubs.ExistingClubResponseModel;
import models.clubs.ResultsClubModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.club.CreateClubSpec.existingCreateClubResponseSpec;
import static specs.club.CreateClubSpec.successfulCreateClubResponseSpec;

public class CreateClubsApiClient {

    @Step("Создать книжный клуб")
    public ResultsClubModel createClub(CreateClubRequestModel body, String actualAccess) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + actualAccess)
                .body(body)
                .when()
                .post("/clubs/")
                .then()
                .spec(successfulCreateClubResponseSpec)
                .extract()
                .as(ResultsClubModel.class);
    }

    @Step("Повторно создать книжный клуб")
    public ExistingClubResponseModel createExistingClub(CreateClubRequestModel body, String actualAccess) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + actualAccess)
                .body(body)
                .when()
                .post("/clubs/")
                .then()
                .spec(existingCreateClubResponseSpec)
                .extract()
                .as(ExistingClubResponseModel.class);
    }
}

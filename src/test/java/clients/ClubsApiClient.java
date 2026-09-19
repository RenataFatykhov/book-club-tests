package clients;

import io.qameta.allure.Step;
import models.clubs.ClubResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.club.ClubSpec.getClubResponseSpec;

public class ClubsApiClient {

    @Step("Получить список клубов: запрос без параметров")
    public void noParametersGetClubs() {
        given(requestSpec)
                .when()
                .get("/clubs/")
                .then()
                .spec(getClubResponseSpec)
                .extract().as(ClubResponseModel.class);
    }
}

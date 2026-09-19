package clients;

import io.qameta.allure.Step;
import models.clubs.ClubRequestPaginationModel;
import models.clubs.ClubResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.club.ClubSpec.getClubResponseSpec;

public class ClubsApiClient {

    @Step("Получить список клубов: запрос без параметров")
    public ClubResponseModel noParametersGetClubs() {
        return given(requestSpec)
                .when()
                .get("/clubs/")
                .then()
                .spec(getClubResponseSpec)
                .extract()
                .as(ClubResponseModel.class);
    }

    @Step("Получить список клубов с параметрами пагинации")
    public ClubResponseModel paginationGetClubs(ClubRequestPaginationModel parameters) {
        return given(requestSpec)
                .queryParam("page", parameters.page())
                .queryParam("page_size", parameters.page_size())
                .when()
                .get("/clubs/")
                .then()
                .spec(getClubResponseSpec)
                .extract()
                .as(ClubResponseModel.class);
    }
}

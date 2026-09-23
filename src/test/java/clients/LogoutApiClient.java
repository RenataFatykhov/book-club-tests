package clients;

import io.qameta.allure.Step;
import models.logout.LogoutRequestModel;
import models.logout.WrongTokenLogoutResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.logout.LogoutSpec.successfulLogoutResponseSpec;
import static specs.logout.LogoutSpec.wrongTokenLogoutResponseSpec;

public class LogoutApiClient {

    @Step("Выполнить logout с refresh-токеном")
    public void logout(LogoutRequestModel body) {
        given(requestSpec)
                .body(body)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    @Step("Выполнить logout с недействительным токеном")
    public WrongTokenLogoutResponseModel wrongTokenLogout(LogoutRequestModel body) {
        return given(requestSpec)
                .body(body)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(wrongTokenLogoutResponseSpec)
                .extract()
                .as(WrongTokenLogoutResponseModel.class);
    }
}

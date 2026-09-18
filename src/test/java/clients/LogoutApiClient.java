package clients;

import models.logout.LogoutRequestModel;
import models.logout.WrongTokenLogoutResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.logout.LogoutSpec.successfulLogoutResponseSpec;
import static specs.logout.LogoutSpec.wrongTokenLogoutResponseSpec;

public class LogoutApiClient {

    public void logout(LogoutRequestModel body){
        given(requestSpec)
                .body(body)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    public WrongTokenLogoutResponseModel wrongTokenLogout(LogoutRequestModel body){
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

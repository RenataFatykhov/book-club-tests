package steps;

import models.login.LoginRequestModel;
import models.login.SuccessfulLoginResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.login.LoginSpec.successLoginResponseSpec;

public class AuthSteps {

    public SuccessfulLoginResponseModel login(String username, String password) {
        LoginRequestModel loginData = new LoginRequestModel(username, password);

        return given(requestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successLoginResponseSpec)
                .extract()
                .as(SuccessfulLoginResponseModel.class);
    }
}

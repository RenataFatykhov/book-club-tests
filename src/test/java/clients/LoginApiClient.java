package clients;

import models.login.*;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.login.LoginSpec.*;

public class LoginApiClient {

    public SuccessfulLoginResponseModel login(LoginRequestModel body){
        return given(requestSpec)
                .body(body)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);
    }

    public WrongCredentialsLoginResponseModel wrongCredentialsLogin(LoginRequestModel body){
        return given(requestSpec)
                .body(body)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec)
                .extract().as(WrongCredentialsLoginResponseModel.class);
    }

    public EmptyUsernameLoginResponseModel emptyUsernameLogin(EmptyUsernameLoginRequestModel body){
        return given(requestSpec)
                .body(body)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyUsernameLoginResponseSpec)
                .extract().as(EmptyUsernameLoginResponseModel.class);
    }

    public EmptyPasswordLoginResponseModel emptyPasswordLogin(EmptyPasswordLoginRequestModel body){
        return given(requestSpec)
                .body(body)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyPasswordLoginResponseSpec)
                .extract().as(EmptyPasswordLoginResponseModel.class);
    }
}

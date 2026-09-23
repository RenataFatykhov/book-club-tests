package clients;

import io.qameta.allure.Step;
import models.login.*;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.login.LoginSpec.*;

public class LoginApiClient {

    @Step("Выполнить вход с корректными учётными данными")
    public SuccessfulLoginResponseModel login(LoginRequestModel body) {
        return given(requestSpec)
                .body(body)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);
    }

    @Step("Выполнить вход с неверным паролем")
    public WrongCredentialsLoginResponseModel wrongCredentialsLogin(LoginRequestModel body) {
        return given(requestSpec)
                .body(body)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec)
                .extract().as(WrongCredentialsLoginResponseModel.class);
    }

    @Step("Отправить запрос входа без поля username")
    public EmptyUsernameLoginResponseModel emptyUsernameLogin(EmptyUsernameLoginRequestModel body) {
        return given(requestSpec)
                .body(body)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyUsernameLoginResponseSpec)
                .extract().as(EmptyUsernameLoginResponseModel.class);
    }

    @Step("Отправить запрос входа без поля password")
    public EmptyPasswordLoginResponseModel emptyPasswordLogin(EmptyPasswordLoginRequestModel body) {
        return given(requestSpec)
                .body(body)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyPasswordLoginResponseSpec)
                .extract().as(EmptyPasswordLoginResponseModel.class);
    }
}

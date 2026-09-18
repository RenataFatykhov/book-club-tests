package clients;

import io.qameta.allure.Step;
import models.registration.ErrorResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.requestWithoutContentTypeSpec;
import static specs.registration.RegistrationSpec.*;

public class RegistrationApiClient {

    @Step("Зарегистрировать пользователя")
    public RegistrationResponseModel register(RegistrationBodyModel body){
        return given(requestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(successRegistrationResponseSpec)
                .extract()
                .as(RegistrationResponseModel.class);
    }

    @Step("Отправить запрос регистрации с занятым username")
    public ErrorResponseModel registerExistingUser(RegistrationBodyModel body){
        return given(requestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(error400RegistrationResponseSpec)
                .extract()
                .as(ErrorResponseModel.class);
    }

    @Step("Отправить запрос регистрации с недопустимым username")
    public ErrorResponseModel registerInvalidUser(RegistrationBodyModel body){
        return given(requestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(error400RegistrationResponseSpec)
                .extract()
                .as(ErrorResponseModel.class);
    }

    @Step("Отправить запрос регистрации без явно заданного Content-Type")
    public void registerWithoutContentType(RegistrationBodyModel body){
        given(requestWithoutContentTypeSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(error415RegistrationResponseSpec);
    }

    @Step("Отправить запрос регистрации без завершающего слеша")
    public void registerWithoutSlash(RegistrationBodyModel body){
        given(requestWithoutContentTypeSpec)
                .body(body)
                .when()
                .post("/users/register")
                .then()
                .spec(redirect301RegistrationResponseSpec);
    }
}

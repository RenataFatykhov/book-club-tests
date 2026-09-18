package clients;

import io.restassured.response.Response;
import models.registration.ErrorResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.requestWithoutContentTypeSpec;
import static specs.registration.RegistrationSpec.*;

public class RegistrationApiClient {

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

    public void registerWithoutContentType(RegistrationBodyModel body){
        given(requestWithoutContentTypeSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(error415RegistrationResponseSpec);
    }

    public void registerWithoutSlash(RegistrationBodyModel body){
        given(requestWithoutContentTypeSpec)
                .body(body)
                .when()
                .post("/users/register")
                .then()
                .spec(redirect301RegistrationResponseSpec);
    }
}

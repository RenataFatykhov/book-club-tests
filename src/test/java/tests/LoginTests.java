package tests;

import models.login.SuccessfulLoginReponseModel;
import models.login.LoginRequestModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class LoginTests extends TestBase {
    String username = "renata";
    String password = "renata123456";
    String wrongPassword = "renata1234567";

    @Test
    public void successfulLoginTest() {

        LoginRequestModel loginData = new LoginRequestModel(username, password);

        SuccessfulLoginReponseModel loginResponse = given()
                .log().all()
                .contentType("application/json")
                .body(loginData)
                .basePath("/api/v1")
                .when()
                .post("/auth/token/")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/login/successful_login_response_schema.json"))
                .body("access", notNullValue())
                .body("refresh", notNullValue())
                .extract().as(SuccessfulLoginReponseModel.class);

        String expectedTokenPath = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();

        assertThat(actualAccess).startsWith(expectedTokenPath);
        assertThat(actualRefresh).startsWith(expectedTokenPath);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    public void wrongCredentialsLoginTest() {

        LoginRequestModel loginData = new LoginRequestModel(username, wrongPassword);

        WrongCredentialsLoginResponseModel loginResponse = given()
                .log().all()
                .contentType("application/json")
                .body(loginData)
                .basePath("/api/v1")
                .when()
                .post("/auth/token/")
                .then()
                .log().all()
                .statusCode(401)
                .body(matchesJsonSchemaInClasspath(
                        "schemas/login/wrong_credentials_login_response_schema.json"))
                .body("detail", notNullValue())
                .extract().as(WrongCredentialsLoginResponseModel.class);

        String expectedDetailError = "Invalid username or password.";
        String actualDetail = loginResponse.detail();

        assertThat(actualDetail).isEqualTo(expectedDetailError);
    }
}

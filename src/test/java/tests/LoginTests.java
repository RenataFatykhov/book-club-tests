package tests;

import models.login.LoginRequestModel;
import models.login.SuccessfulLoginReponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.requestSpec;
import static specs.login.LoginSpec.*;

public class LoginTests extends TestBase {
    String username = "renata";
    String password = "renata123456";
    String wrongPassword = "renata1234567";

    @Test
    public void successfulLoginTest() {

        LoginRequestModel loginData = new LoginRequestModel(username, password);

        SuccessfulLoginReponseModel loginResponse = given(requestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successLoginResponseSpec)
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

        WrongCredentialsLoginResponseModel loginResponse = given(requestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec)
                .extract().as(WrongCredentialsLoginResponseModel.class);

        String expectedDetailError = "Invalid username or password.";
        String actualDetail = loginResponse.detail();

        assertThat(actualDetail).isEqualTo(expectedDetailError);
    }
}

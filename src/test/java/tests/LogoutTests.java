package tests;

import models.login.LoginRequestModel;
import models.logout.LogoutRequestModel;
import models.logout.LogoutResponseModel;
import models.logout.WrongTokenLogoutResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.BaseSpec.requestSpec;
import static specs.login.LoginSpec.successLoginResponseSpec;
import static specs.logout.LogoutSpec.successfulLogoutResponseSpec;
import static specs.logout.LogoutSpec.wrongTokenLogoutResponseSpec;
import static specs.registration.RegistrationSpec.successRegistrationResponseSpec;

public class LogoutTests extends TestBase {
    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        username = faker.name().firstName();
        password = faker.credentials().password();
    }

    @Test
    public void successfulLogoutTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        given(requestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successRegistrationResponseSpec)
                .extract()
                .as(RegistrationResponseModel.class);

        LoginRequestModel loginData = new LoginRequestModel(username, password);

        String refreshToken = given(requestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successLoginResponseSpec)
                .extract().path("refresh");


        LogoutRequestModel logoutRequestModel = new LogoutRequestModel(refreshToken);

        LogoutResponseModel logoutResponse = given(requestSpec)
                .body(logoutRequestModel)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec)
                .extract()
                .as(LogoutResponseModel.class);

    }

    @Test
    public void wrongTokenLogoutTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        given(requestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successRegistrationResponseSpec)
                .extract()
                .as(RegistrationResponseModel.class);

        LoginRequestModel loginData = new LoginRequestModel(username, password);

        String accessToken = given(requestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successLoginResponseSpec)
                .extract().path("access");


        LogoutRequestModel logoutRequestModel = new LogoutRequestModel(accessToken);

        WrongTokenLogoutResponseModel logoutResponse = given(requestSpec)
                .body(logoutRequestModel)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(wrongTokenLogoutResponseSpec)
                .extract()
                .as(WrongTokenLogoutResponseModel.class);

        String expectedDetail = "Token has wrong type";
        String actualDetail = logoutResponse.detail();

        String expectedCode = "token_not_valid";
        String actualCode = logoutResponse.code();

        assertThat(actualDetail).isEqualTo(expectedDetail);
        assertThat(actualCode).isEqualTo(expectedCode);

    }
}

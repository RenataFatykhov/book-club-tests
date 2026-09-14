package tests;

import models.login.LoginRequestModel;
import models.logout.LogoutRequestModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.login.LoginSpec.successLoginResponseSpec;
import static specs.logout.LogoutSpec.successfulLogoutResponseSpec;
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

        given(requestSpec)
                .body(logoutRequestModel)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec)
                .statusCode(200);

    }
}

package tests;

import models.login.LoginRequestModel;
import models.login.SuccessfulLoginReponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;
import models.update.user.AuthErrorResponseModel;
import models.update.user.SuccessfulUserUpdateResponseModel;
import models.update.user.UserUpdateRequestModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.requestSpec;
import static specs.login.LoginSpec.successLoginResponseSpec;
import static specs.registration.RegistrationSpec.successRegistrationResponseSpec;
import static specs.update.user.UserUpdateSpec.authErrorUserUpdateResponseSpec;
import static specs.update.user.UserUpdateSpec.successfulUserUpdateResponseSpec;

public class UpdateUserTests extends TestBase {
    String username;
    String newUsername;
    String password;
    String firstName;
    String lastName;
    String email;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        username = faker.name().firstName();
        newUsername = faker.name().firstName();
        password = faker.credentials().password();
        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        email = faker.internet().emailAddress();

    }

    @Test
    public void successfulPutUpdateUserTest() {
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

        SuccessfulLoginReponseModel loginResponse = given(requestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successLoginResponseSpec)
                .extract().as(SuccessfulLoginReponseModel.class);

        String actualAccess = loginResponse.access();

        UserUpdateRequestModel userUpdateData = new UserUpdateRequestModel(newUsername, firstName, lastName, email);

        SuccessfulUserUpdateResponseModel updateResponse = given(requestSpec)
                .header("Authorization", "Bearer " + actualAccess)
                .body(userUpdateData)
                .when()
                .put("/users/me/")
                .then()
                .spec(successfulUserUpdateResponseSpec)
                .extract().as(SuccessfulUserUpdateResponseModel.class);

        String expectedUsername = userUpdateData.username();
        String expectedFirstName = userUpdateData.firstName();
        String expectedLastName = userUpdateData.lastName();
        String expectedEmail = userUpdateData.email();

        String actualUsername = updateResponse.username();
        String actualFirstName = updateResponse.firstName();
        String actualLastName = updateResponse.lastName();
        String actualEmail = updateResponse.email();

        assertThat(actualUsername).isEqualTo(expectedUsername);
        assertThat(actualFirstName).isEqualTo(expectedFirstName);
        assertThat(actualLastName).isEqualTo(expectedLastName);
        assertThat(actualEmail).isEqualTo(expectedEmail);

    }

    @Test
    public void authErrorPutUpdateUserTest() {
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

        SuccessfulLoginReponseModel loginResponse = given(requestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successLoginResponseSpec)
                .extract().as(SuccessfulLoginReponseModel.class);


        UserUpdateRequestModel userUpdateData = new UserUpdateRequestModel(newUsername, firstName, lastName, email);

        AuthErrorResponseModel updateResponse = given(requestSpec)
                .body(userUpdateData)
                .when()
                .put("/users/me/")
                .then()
                .spec(authErrorUserUpdateResponseSpec)
                .extract().as(AuthErrorResponseModel.class);

        String expectedDetail = "Authentication credentials were not provided.";
        String actualDetail = updateResponse.detail();

        assertThat(actualDetail).isEqualTo(expectedDetail);

    }


}

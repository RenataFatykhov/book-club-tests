package tests;

import models.login.LoginRequestModel;
import models.login.SuccessfulLoginReponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;
import models.update.user.AuthErrorResponseModel;
import models.update.user.SuccessfulUserUpdateResponseModel;
import models.update.user.UserUpdateRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static data.TestData.*;
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
        username = generateUsername();
        newUsername = generateNewUsername();
        password = generatePassword();
        firstName = generateFirstName();
        lastName = generateLastName();
        email = generateEmail();
    }

    @Test
    @DisplayName("Успешный апдейт юзера")
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
    @DisplayName("Отправка запроса без необходимого заголовка авторизации")
    public void authErrorPutUpdateUserTest() {

        UserUpdateRequestModel userUpdateData = new UserUpdateRequestModel(newUsername, firstName, lastName, email);

        AuthErrorResponseModel updateResponse = given(requestSpec)
                .body(userUpdateData)
                .when()
                .put("/users/me/")
                .then()
                .spec(authErrorUserUpdateResponseSpec)
                .extract().as(AuthErrorResponseModel.class);

        String actualDetail = updateResponse.detail();

        assertThat(actualDetail).isEqualTo(EXPECTED_UPDATE_AUTH_DETAIL);

    }


}

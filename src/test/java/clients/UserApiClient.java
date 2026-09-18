package clients;

import models.update.user.AuthErrorResponseModel;
import models.update.user.SuccessfulUserUpdateResponseModel;
import models.update.user.UserUpdateRequestModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.update.user.UserUpdateSpec.authErrorUserUpdateResponseSpec;
import static specs.update.user.UserUpdateSpec.successfulUserUpdateResponseSpec;

public class UserApiClient {

    public SuccessfulUserUpdateResponseModel updateUser(UserUpdateRequestModel body, String actualAccess) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + actualAccess)
                .body(body)
                .when()
                .put("/users/me/")
                .then()
                .spec(successfulUserUpdateResponseSpec)
                .extract().as(SuccessfulUserUpdateResponseModel.class);
    }

    public AuthErrorResponseModel authErrorUpdateUser(UserUpdateRequestModel body) {
        return given(requestSpec)
                .body(body)
                .when()
                .put("/users/me/")
                .then()
                .spec(authErrorUserUpdateResponseSpec)
                .extract().as(AuthErrorResponseModel.class);
    }
}

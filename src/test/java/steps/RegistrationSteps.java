package steps;

import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.registration.RegistrationSpec.successRegistrationResponseSpec;

public class RegistrationSteps {
    public RegistrationResponseModel registerUser(String username, String password) {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        return given(requestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successRegistrationResponseSpec)
                .extract()
                .as(RegistrationResponseModel.class);

    }
}

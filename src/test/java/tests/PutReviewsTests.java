package tests;

import models.clubs.ClubResponseModel;
import models.clubs.review.UnauthReviewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.AuthSteps;
import steps.RegistrationSteps;

import static data.TestData.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.requestSpec;

public class PutReviewsTests extends TestBase {
    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        username = generateUsername();
        password = generatePassword();
    }

    @Test
    @DisplayName("Обновление отзыва возвращает 401 для неавторизированного запроса")
    public void unauthorizedPutReviewsTest() {

        UnauthReviewModel response = given(requestSpec)
                .when()
                .put("/clubs/reviews/")
                .then()
                .log().all()
                .statusCode(401)
                .extract().response().as(UnauthReviewModel.class);

        assertThat(response.detail()).isEqualTo(EXPECTED_NOT_AUTH_DETAIL);
    }

    @Test
    @Disabled
    @DisplayName("Получение списка отзывов возвращает 200")
    public void successfulPutReviewsTest() {

        RegistrationSteps registrationSteps = new RegistrationSteps();
        registrationSteps.registerUser(username, password);

        AuthSteps authSteps = new AuthSteps();
        String actualAccess = authSteps.login(username, password).access();

        ClubResponseModel response = given(requestSpec)
                .header("Authorization", "Bearer " + actualAccess)
                .when()
                .get("/clubs/reviews/")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().as(ClubResponseModel.class);

        assertThat(response.results()).isNotEmpty();
    }


}

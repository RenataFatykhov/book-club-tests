package tests;

import models.registration.ErrorResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static data.TestData.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class RegistrationTests extends TestBase {
    String username;
    String password;
    String invalidUsername;

    @BeforeEach
    public void prepareTestData() {
        username = generateUsername();
        password = generatePassword();
        invalidUsername = generateInvalidUsername();
    }

    @Test
    @DisplayName("Регистрация с новым username создаёт пользователя и возвращает 201")
    public void successfulRegistrationTest() {

        RegistrationBodyModel body = new RegistrationBodyModel(username, password);

        RegistrationResponseModel response = registrationClient.register(body);

        assertThat(response.username()).isEqualTo(username);
        assertThat(response.id()).isGreaterThan(0);
        assertThat(response.firstName()).isEqualTo("");
        assertThat(response.lastName()).isEqualTo("");
        assertThat(response.email()).isEqualTo("");
        assertThat(response.remoteAddr()).matches(IP_ADDR_REGEXP);

    }

    @Test
    @DisplayName("Регистрация с занятым username возвращает 400")
    public void existingUserWrongRegistrationTest() {

        RegistrationBodyModel body = new RegistrationBodyModel(username, password);

        step("Зарегистрировать пользователя с новым username", () -> {
            RegistrationResponseModel firstResponse = registrationClient.register(body);

            assertThat(firstResponse.username()).isEqualTo(username);
        });

        step("Повторить регистрацию с тем же username и проверить 400", () -> {
            ErrorResponseModel secondResponse = registrationClient.registerExistingUser(body);

            String actualErrorMessage = secondResponse.username().get(0);
            assertThat(actualErrorMessage).isEqualTo(EXPECTED_EXISTING_USER_ERROR_MESSAGE);
        });
    }

    @Test
    @DisplayName("Регистрация с недопустимыми символами в username возвращает 400")
    public void invalidUsername400Test() {

        RegistrationBodyModel body = new RegistrationBodyModel(username, password);

        ErrorResponseModel response = registrationClient.registerInvalidUser(body);

        assertEquals(EXPECTED_INVALID_USERNAME_ERROR_MESSAGE, response.username().get(0));

    }

    @Test
    @DisplayName("Регистрация без явно заданного Content-Type возвращает 415")
    public void negativeRegistration415Test() {

        RegistrationBodyModel body = new RegistrationBodyModel(username, password);

        registrationClient.registerWithoutContentType(body);

    }

    @Test
    @DisplayName("Запрос регистрации без завершающего слеша возвращает 301")
    public void negativeRegistration301Test() {

        RegistrationBodyModel body = new RegistrationBodyModel(username, password);

        registrationClient.registerWithoutSlash(body);

    }
}

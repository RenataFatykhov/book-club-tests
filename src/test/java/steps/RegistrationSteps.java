package steps;

import clients.RegistrationApiClient;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseModel;

public class RegistrationSteps {

    private final RegistrationApiClient registrationClient =
            new RegistrationApiClient();

    public RegistrationResponseModel registerUser(String username, String password) {
        RegistrationBodyModel body = new RegistrationBodyModel(username, password);

        return registrationClient.register(body);

    }
}

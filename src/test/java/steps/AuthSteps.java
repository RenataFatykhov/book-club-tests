package steps;

import clients.LoginApiClient;
import models.login.LoginRequestModel;
import models.login.SuccessfulLoginResponseModel;

public class AuthSteps {

    private final LoginApiClient loginClient = new LoginApiClient();

    public SuccessfulLoginResponseModel login(String username, String password) {

        LoginRequestModel loginData = new LoginRequestModel(username, password);

        return loginClient.login(loginData);
    }
}

package tests;

import clients.*;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class TestBase {

    protected RegistrationApiClient registrationClient;
    protected LoginApiClient loginClient;
    protected LogoutApiClient logoutClient;
    protected UserApiClient userClient;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://book-club.qa.guru";
    }

    @BeforeEach
    public void setUpClients(){
        registrationClient = new RegistrationApiClient();
        loginClient = new LoginApiClient();
        logoutClient = new LogoutApiClient();
        userClient = new UserApiClient();
    }

}

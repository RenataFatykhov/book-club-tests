package tests;

import clients.*;
import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class TestBase {

    protected RegistrationApiClient registrationClient;
    protected LoginApiClient loginClient;
    protected LogoutApiClient logoutClient;
    protected UserApiClient userClient;
    protected ClubsApiClient clubsClient;
    protected CreateClubsApiClient createClubsClient;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://book-club.qa.guru";
        RestAssured.basePath = "/api/v1";


        Configuration.baseUrl = "https://book-club.qa.guru";
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    public void setUpClients() {
        registrationClient = new RegistrationApiClient();
        loginClient = new LoginApiClient();
        logoutClient = new LogoutApiClient();
        userClient = new UserApiClient();
        clubsClient = new ClubsApiClient();
        createClubsClient = new CreateClubsApiClient();
    }

}

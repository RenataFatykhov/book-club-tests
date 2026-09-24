package tests;

import clients.*;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;

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

    @BeforeEach
    void setUpAllure() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }


    @AfterEach
    void tearDown() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
        closeWebDriver();
    }

}

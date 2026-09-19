package tests;

import models.clubs.ClubResponseModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static specs.BaseSpec.requestSpec;
import static specs.club.ClubSpec.getClubResponseSpec;

public class ClubsTests extends TestBase {

    int count;
    String next;
    List<ClubResponseModel> results;

    @Test
    public void getClubsWithoutParametersTest() {
        clubsClient.noParametersGetClubs();
    }
}

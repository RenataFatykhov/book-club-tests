package models.clubs;

import java.util.List;

public record ClubResponseModel(
        int count,
        String next,
        String previous,
        List<ResultsClubModel> results
) {
}
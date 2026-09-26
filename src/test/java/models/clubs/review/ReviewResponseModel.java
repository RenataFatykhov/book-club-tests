package models.clubs.review;

import java.util.List;

public record ReviewResponseModel(
        int count,
        String next,
        String previous,
        List<ReviewModel> results
) {
}
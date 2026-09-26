package models.clubs;

import models.clubs.review.ReviewModel;

import java.util.List;

public record ResultsClubModel(
        Integer id,
        String bookTitle,
        String bookAuthors,
        Integer publicationYear,
        String description,
        String telegramChatLink,
        Integer owner,
        List<Integer> members,
        List<ReviewModel> reviews,
        String created,
        String modified
) {
}

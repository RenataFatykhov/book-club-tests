package models.clubs;

public record CreateClubRequestModel(
        String bookTitle,
        String bookAuthors,
        int publicationYear,
        String description,
        String telegramChatLink
) {
}

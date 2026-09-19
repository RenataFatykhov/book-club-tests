package models.clubs;

public record ClubRequestModel(
        String membership,
        int page,
        int page_size,
        String search
) {
}
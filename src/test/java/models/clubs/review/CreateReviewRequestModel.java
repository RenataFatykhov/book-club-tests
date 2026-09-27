package models.clubs.review;

public record CreateReviewRequestModel(
        Integer club,
        String review,
        Integer assessment,
        Integer readPages
) {
}

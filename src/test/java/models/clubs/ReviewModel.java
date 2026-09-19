package models.clubs;

public record ReviewModel(
        Integer id,
        Integer club,
        UserModel user,
        String review,
        Integer assessment,
        Integer readPages,
        String created,
        String modified
) {
}

package models.update.user;

public record SuccessfulUserUpdateResponseModel
        (int id, String username, String firstName, String lastName, String email, String remoteAddr) {
}

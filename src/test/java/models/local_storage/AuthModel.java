package models.local_storage;

public record AuthModel(
        UserModel user,
        String accessToken,
        String refreshToken,
        boolean isAuthenticated
) {
}

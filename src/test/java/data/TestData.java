package data;

import net.datafaker.Faker;

import java.util.UUID;

public class TestData {
    private static final Faker faker = new Faker();

    // random
    public static String generateUsername() {
        return "autotest_" + UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateNewUsername(String currentUsername) {
        return currentUsername + "_updated";
    }

    public static String generateFirstName() {
        return faker.name().firstName();
    }

    public static String generateLastName() {
        return faker.name().lastName();
    }

    public static String generatePassword() {
        return faker.credentials().password();
    }

    public static String generateWrongPassword() {
        return faker.credentials().password();
    }

    public static String generateInvalidUsername() {
        return faker.name().fullName();
    }

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }


    // registration
    public static final String IP_ADDR_REGEXP =
            "^(?:(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}"
                    + "(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";
    public static final String EXPECTED_EXISTING_USER_ERROR_MESSAGE = "A user with that username already exists.";
    public static final String EXPECTED_INVALID_USERNAME_ERROR_MESSAGE =
            "Enter a valid username. This value may contain only letters, numbers, and @/./+/-/_ characters.";


    // login
    public static final String EXPECTED_TOKEN_PATH = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    public static final String EXPECTED_LOGIN_ERROR_DETAIL = "Invalid username or password.";
    public static final String EXPECTED_USERNAME_ERROR = "This field is required.";
    public static final String EXPECTED_PASSWORD_ERROR = "This field is required.";

    // logout
    public static final String EXPECTED_WRONG_TOKEN_DETAIL = "Token has wrong type";
    public static final String EXPECTED_BLOCKED_TOKEN_DETAIL = "Token is blacklisted";
    public static final String EXPECTED_TOKEN_ERROR_CODE = "token_not_valid";

    // update
    public static final String EXPECTED_UPDATE_AUTH_DETAIL = "Authentication credentials were not provided.";
}

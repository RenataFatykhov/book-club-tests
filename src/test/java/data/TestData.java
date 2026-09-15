package data;

import net.datafaker.Faker;

public class TestData {
    private static final Faker faker = new Faker();

    // random
    public static String setUsername() {
        return faker.name().firstName();
    }

    public static String setNewUsername() {
        return faker.name().firstName();
    }

    public static String setFirstName() {
        return faker.name().firstName();
    }

    public static String setLastName() {
        return faker.name().lastName();
    }

    public static String setPassword() {
        return faker.credentials().password();
    }

    public static String setWrongPassword() {
        return faker.credentials().password();
    }

    public static String setInvalidUsername() {
        return faker.name().fullName();
    }

    public static String setEmail() {
        return faker.internet().emailAddress();
    }


    // registration
    public static final String ipAddrRegexp =
            "^(?:(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}"
                    + "(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";
    public static final String expectedExistUserErrorMessage = "A user with that username already exists.";
    public static final String expectedInvalidUsernameErrorMessage =
            "Enter a valid username. This value may contain only letters, numbers, and @/./+/-/_ characters.";


    // login
    public static final String expectedTokenPath = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    public static final String expectedDetailError = "Invalid username or password.";
    public static final String expectedUsernameError = "This field is required.";
    public static final String expectedPasswordError = "This field is required.";

    // logout
    public static final String expectedDetail = "Token has wrong type";
    public static final String expectedCode = "token_not_valid";

    // update
    public static final String expectedUpdateAuthDetail = "Authentication credentials were not provided.";
}

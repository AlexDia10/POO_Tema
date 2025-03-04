package org.example;

import java.time.LocalDate;
import java.util.Random;

public class UserFactory {
    public static User createUser(AccountType accountType, String name, String country, String email, Gender gender, LocalDate birthDate) {
        String username = generateUsername(name);
        String password = generatePassword();

        User user = switch (accountType) {
            case Regular -> new Regular();
            case Contributor -> new Contributor();
            case Admin -> new Admin();
            default -> throw new IllegalArgumentException("No such user type: " + accountType);
        };

        user.setUsername(username);
        user.setInformation(new User.Information.InformationBuilder(name, country)
                .credentials(new Credentials(email, password))
                .gender(gender)
                .birthDate(birthDate)
                .build());

        return user;
    }

    private static String generateUsername(String name) {
        String baseUsername = name.toLowerCase().replaceAll(" ", "_");

        Random random = new Random();
        int randomInt = random.nextInt(100);

        String uniqueUsername = baseUsername + "_" + randomInt;

        return uniqueUsername;
    }

    public static String generatePassword() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_=+";

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(chars.length());
            password.append(chars.charAt(randomIndex));
        }
        return password.toString();
    }
}

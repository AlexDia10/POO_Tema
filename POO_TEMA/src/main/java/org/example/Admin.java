package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Admin extends Staff {

    public Admin() {
    }

    // Adaugarera / Stergerea unui utilizator (metode specifice Admin)
    public void addUser(User user, List<User> users) {
        boolean validInput = false;
        User newUser = null;

        while (!validInput) {
            try {
                System.out.println("Enter account type (Regular, Contributor, Admin):");
                Scanner scanner = new Scanner(System.in);
                String accountTypeInput = scanner.nextLine();
                AccountType accountType = AccountType.valueOf(accountTypeInput);

                System.out.print("Enter name: ");
                String name = scanner.nextLine();

                System.out.print("Enter country: ");
                String country = scanner.nextLine();

                System.out.print("Enter email: ");
                String email = scanner.nextLine();

                System.out.print("Enter gender (Male/Female/Other): ");
                String genderInput = scanner.nextLine();
                Gender gender = Gender.valueOf(genderInput);

                System.out.print("Enter your birthdate (YYYY-MM-DD): ");
                String birthDateInput = scanner.nextLine();
                LocalDate birthDate = LocalDate.parse(birthDateInput);

                newUser = UserFactory.createUser(accountType, name, country, email, gender, birthDate);

                System.out.println("User created: " + newUser.getUsername());
                System.out.println("[password: " + newUser.getInformation().getCredentials().getPassword() + " ]");

                validInput = true; // If we reach here without exceptions, input is valid
            } catch (IllegalArgumentException | DateTimeParseException e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
        users.add(newUser);

        System.out.println("List of Users:");
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i).getUsername());
        }
    }

    public void removeUser(List<User> users) {
        System.out.println("List of Users:");
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i).getUsername());
        }

        System.out.print("Enter the number of the user to delete: ");
        Scanner scanner = new Scanner(System.in);
        int userIndexToDelete = scanner.nextInt();

        if (userIndexToDelete >= 1 && userIndexToDelete <= users.size()) {
            User userToDelete = users.remove(userIndexToDelete - 1);
            System.out.println("User " + userToDelete.getUsername() + " deleted.");

            for (int i = 0; i < users.size(); i++) {
                System.out.println((i + 1) + ". " + users.get(i).getUsername());
            }
        } else {
            System.out.println("Invalid user index.");
        }
    }

    // Metodele din superclasa User

    @Override
    public void updateExperience(int newExperience) {

    }

    @Override
    public void logout() {

    }

    // Metodele din superclasa Staff
    @Override
    public void resolveRequests(List<Request> requests) {
        super.resolveRequests(requests);
    }

    // Interfata Observer
    @Override
    public void update(String notification) {

    }

    @Override
    public void update(Production production, Rating rating, User observer) {

    }
}

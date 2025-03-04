package org.example;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.example.IMDB.findProductionByTitle;

public class Regular extends User implements RequestManager {

    public Regular() {
    }

    // Add/Delete review (Regular specific method)
    public void addReview(User user) throws IOException {
        System.out.println("Enter the title of the production to review:");
        Scanner scanner = new Scanner(System.in);
        String productionTitle = scanner.nextLine().toLowerCase();

        Production production = findProductionByTitle(productionTitle);

        if (production != null) {
            // Check if the user has already reviewed the production
            Rating userRating = production.findReviewByUser(user);

            if (userRating != null) {
                System.out.println("Your existing review:");
                userRating.displayInfo();

                System.out.println("\nYou need to delete the existing review before adding a new one.");
            } else {
                System.out.println("No existing rating. Add rating:");

                int rating = 0;
                boolean validInput = false;
                do {
                    System.out.print("Your rating [1-10]: ");

                    if (scanner.hasNextInt()) {
                        rating = scanner.nextInt();

                        // Check if the rating is within the valid range [1-10]
                        if (rating >= 1 && rating <= 10) {
                            validInput = true;
                        } else {
                            System.out.println("Please enter a rating between 1 and 10.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter a number.");
                        scanner.nextLine();
                    }
                } while (!validInput);
                scanner.nextLine();

                System.out.print("Your comments: ");
                String comments = scanner.nextLine();

                // Create a new review
                Rating newRating = new Rating(user.getUsername(), rating, comments);

                production.notifyObservers(newRating, production);
                production.addObserver(user);

                // Add the new review to the production
                production.addReview(newRating);
                System.out.println("New review added!");
            }
        } else {
            System.out.println("Production not found.");
        }
    }

    public void removeReview(User user) throws IOException {
        System.out.println("Enter the title of the production you want to remove the review from:");
        Scanner scanner = new Scanner(System.in);
        String productionTitle = scanner.nextLine().toLowerCase();

        Production production = findProductionByTitle(productionTitle);

        if (production != null) {
            // Check if the user has already reviewed the production
            Rating userRating = production.findReviewByUser(user);

            if (userRating != null) {
                System.out.println("Your existing review:");
                userRating.displayInfo();

                production.removeReview(user);
                production.removeObserver(user);

                System.out.println("Review removed!");
            } else {
                System.out.println("You haven't reviewed this production.");
            }
        } else {
            System.out.println("Production not found.");
        }
    }

    // Add/Delete a request
    static String getRequestsList() {
        StringBuilder requests = new StringBuilder();
        for (RequestType request : RequestType.values()) {
            requests.append(request).append(", ");
        }
        return requests.substring(0, requests.length() - 2); // Remove the trailing comma and space
    }

    @Override
    public void createRequest(List<Request> requests, User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter request details:");

        System.out.println("Possible requests: " + getRequestsList());

        RequestType type = null;
        while (type == null) {
            System.out.print("Type: ");
            String inputType = scanner.nextLine();

            try {
                RequestType selectedRequest = RequestType.valueOf(inputType);
                type = selectedRequest; // Set type only if it's a valid request
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter a valid request from the list.");
            }
        }

        System.out.print("Description: ");
        String description = scanner.nextLine();

        Request newRequest = new Request(type, LocalDateTime.now(), user.getUsername(), "admin", description);
        requests.add(newRequest);

        System.out.println("Request added successfully.");
    }

    @Override
    public void removeRequest(List<Request> requests, User user) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Your requests:");
        for (int i = 0; i < requests.size(); i++) {
            Request request = requests.get(i);
            if (user.getUsername().equals(request.getUsername())) {
                System.out.println(i + ")");
                request.displayInfo();
            }
        }

        if (requests.isEmpty()) {
            System.out.println("No previous requests.");
            return;
        }

        System.out.print("Enter the index of the request to remove: ");
        int indexToRemove = scanner.nextInt();
        scanner.nextLine();

        if (indexToRemove >= 0 && indexToRemove < requests.size()) {
            Request requestToRemove = requests.get(indexToRemove);

            if (user.getUsername().equals(requestToRemove.getUsername())) {
                requests.remove(indexToRemove);
                System.out.println("Request removed successfully.");
            } else {
                System.out.println("You can only remove requests added by you.");
            }
        } else {
            System.out.println("Invalid index. Please try again.");
        }
    }

    // Metodele din superclasa User
    @Override
    public void updateExperience(int newExperience) {

    }

    @Override
    public void logout() {

    }

    // Interfata Observer
    @Override
    public void update(Production production, Rating rating, User observer) {
        String notification = "The production you reviewed: " + production.getProductionTitle() + " has received a new review from " + rating.getUsername() + ".";
        observer.addNotification(notification);
    }

    @Override
    public void update(String notification) {

    }
}

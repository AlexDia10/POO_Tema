package org.example;

import java.time.LocalDateTime;
import java.util.*;

import static org.example.Regular.getRequestsList;

public class Contributor extends Staff implements RequestManager {

    public Contributor() {
    }

    // Metode din interfata RequestManager
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
        String notification = "The production: " + production.getProductionTitle() + " has received a new review from " + rating.getUsername() + ".";
        observer.addNotification(notification);
    }
}

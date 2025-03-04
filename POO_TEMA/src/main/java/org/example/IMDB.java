package org.example;

import java.io.*;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class IMDB {
    private static List<User> users; // List with Regular, Contributor and Admin elements
    private static List<Actor> actors;
    private static List<Request> requests;
    private static List<Production> productions; // List with Movies and Series elements

    public static List<User> getUsers() {
        return users;
    }

    public static IMDB instance;
    // Private constructor to restrict instantiation of the class from other classes
    private IMDB(){}
    // Lazy initialization
    public static IMDB getInstance() throws IOException {
        if (instance == null) {
            instance = new IMDB();
            instance.users = readUsersFromFile();
            instance.actors = readActorsFromFile();
            instance.productions = readProductionsFromFile();
            instance.requests = readRequestsFromFile();
        }
        return instance;
    }

    public static void run() throws IOException {

        // Data from the JSON files is loaded
        productions = readProductionsFromFile();
        actors = readActorsFromFile();
        users = readUsersFromFile();
        requests = readRequestsFromFile();

        Scanner scanner = new Scanner(System.in);

        User authenticatedUser = null;

        while (true) {
            if (authenticatedUser == null) {
                authenticatedUser = login();

                if (authenticatedUser != null) {
                    System.out.println("Welcome back user " + authenticatedUser.getUsername() + "!");
                    System.out.println("Username: " + authenticatedUser.getUsername());
                    System.out.println("Experience: " + authenticatedUser.getExperience());
                } else {
                    System.out.println("Invalid credentials. Please try again.");
                    continue;
                }
            }

            int choice;
            int logoutNumber;

            do {
                choice = -1;

                logoutNumber = printMenu(authenticatedUser);

                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("The input is not an integer!");
                    scanner.nextLine();
                }

                if (choice == logoutNumber) {
                    System.out.println("Logging out...");
                    authenticatedUser = null; // Reset authenticatedUser to allow login again
                    break;
                } else {
                    processChoice(authenticatedUser, choice);
                }
            } while (choice != 0);
        }
    }

    // THE APPLICATION STARTS HERE
    public static void main(String[] args) throws IOException {
        run();
    }

    public static List<User> readUsersFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(User.class, new UserDeserializer());
        objectMapper.registerModule(module);

        return objectMapper.readValue(new File("src/main/resources/input/accounts.json"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
    }

    private static List<Actor> readActorsFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Actor.class, new ActorDeserializer());
        objectMapper.registerModule(module);

        return objectMapper.readValue(new File("src/main/resources/input/actors.json"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Actor.class));
    }

    private static List<Production> readProductionsFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Production.class, new ProductionDeserializer());
        objectMapper.registerModule(module);

        return objectMapper.readValue(new File("src/main/resources/input/production.json"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Production.class));
    }

    private static List<Request> readRequestsFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Request.class, new RequestDeserializer());
        objectMapper.registerModule(module);

        return objectMapper.readValue(new File("src/main/resources/input/requests.json"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Request.class));
    }

    private static User login() {
        System.out.println("Welcome back! Enter your credentials:");

        System.out.print("email: ");
        Scanner scanner = new Scanner(System.in);
        String enteredEmail = scanner.nextLine();

        System.out.print("password: ");
        String enteredPassword = scanner.nextLine();

        for (User user : users) {
            if (user.isValidCredentials(enteredEmail, enteredPassword)) {
                return user;
            }
        }
        return null;
    }

    private static int printMenu(User user) {
        System.out.println("Choose action:");

        System.out.println("1) View production details");
        System.out.println("2) View actors details");
        System.out.println("3) View notifications");
        System.out.println("4) Search for actor/movie/series");
        System.out.println("5) Add/Delete actor/movie/series to/from favorites");

        int optionCounter = 6;

        if (user instanceof Regular) {
            System.out.println(optionCounter + ") Add/Delete review");
            optionCounter++;
            System.out.println(optionCounter + ") Add/Delete a request");
            optionCounter++;
        } else if (user instanceof Contributor) {
            System.out.println(optionCounter + ") Add/Delete actor/movie/series to/from system");
            optionCounter++;
            System.out.println(optionCounter + ") Update Movie Details");
            optionCounter++;
            System.out.println(optionCounter + ") Update Actor Details");
            optionCounter++;
            System.out.println(optionCounter + ") Solve a request");
            optionCounter++;
            System.out.println(optionCounter + ") Add/Delete a request");
            optionCounter++;
        } else if (user instanceof Admin) {
            System.out.println(optionCounter + ") Add/Delete user");
            optionCounter++;
            System.out.println(optionCounter + ") Add/Delete actor/movie/series to/from system");
            optionCounter++;
            System.out.println(optionCounter + ") Update Movie Details");
            optionCounter++;
            System.out.println(optionCounter + ") Update Actor Details");
            optionCounter++;
            System.out.println(optionCounter + ") Solve a request");
            optionCounter++;
        }

        System.out.println(optionCounter + ") Logout");
        System.out.println("0) Close application");

        return optionCounter;
    }

    private static void processChoice(User user, int choice) throws IOException {
        switch (choice) {
            case 0: {
                System.out.println("Exiting the application...");
                System.exit(0);
                break;
            }
            case 1: {
                // Handle View production details
                System.out.println("Do you want to sort by genre or number of ratings? (genre/ratings/no)");
                Scanner scanner = new Scanner(System.in);
                String sortingChoice = scanner.nextLine().toLowerCase();

                switch (sortingChoice) {
                    case "genre": {
                        productions.sort(Comparator.comparing(p -> p.getGenreList().toString()));
                        break;
                    }
                    case "ratings": {
                        productions.sort(
                                Comparator.<Production, Integer>comparing(
                                        p -> p.getRatingList() != null ? p.getRatingList().size() : 0
                                ).reversed()
                        );
                        break;
                    }
                    default: {
                        System.out.println(("No sorting:\n"));
                        break;
                    }
                }
                for (Production production : productions) {
                    production.displayInfo();
                }
                break;
            }
            case 2: {
                // Handle View actors details
                System.out.println("Do you want to sort by name? (yes/no)");
                Scanner scanner = new Scanner(System.in);
                String sortingChoice = scanner.nextLine().toLowerCase();

                switch (sortingChoice) {
                    case "yes": {
                        actors.sort(Comparator.comparing(Actor::getName));
                        break;
                    }
                    default: {
                        System.out.println(("No sorting:\n"));
                        break;
                    }
                }
                for (Actor actor : actors) {
                    actor.displayInfo();
                }
                break;
            }
            case 3: {
                // Handle View notifications
                user.printNotifications();
                break;
            }
            case 4: {
                // Handle Search for actor/movie/series
                System.out.println("What do you want to search for? (actor/movie/series)");
                Scanner scanner = new Scanner(System.in);
                String searchingChoice = scanner.nextLine().toLowerCase();

                switch (searchingChoice) {
                    case "actor": {
                        System.out.println("Enter the name of the actor you want to search:");
                        String actorChoice = scanner.nextLine().toLowerCase();

                        List<Actor> matchingActors = new ArrayList<>();
                        for (Actor actor : actors) {
                            if (actor.getName().toLowerCase().contains(actorChoice)) {
                                matchingActors.add(actor);
                            }
                        }
                        if (!matchingActors.isEmpty()) {
                            System.out.println("Search result:\n");
                            for (Actor actor : matchingActors) {
                                actor.displayInfo();
                            }
                        } else {
                            System.out.println("No matching actors found.");
                        }
                        break;
                    }
                    case "movie": {
                        System.out.println("Enter the title of the movie you want to search:");
                        String movieTitle = scanner.nextLine().toLowerCase();

                        List<Production> matchingMovies = new ArrayList<>();
                        for (Production movie : productions) {
                            if (movie instanceof Movie && movie.getProductionTitle().toLowerCase().contains(movieTitle)) {
                                matchingMovies.add(movie);
                            }
                        }

                        if (!matchingMovies.isEmpty()) {
                            System.out.println("Search result:\n");
                            for (Production movie : matchingMovies) {
                                if (movie instanceof Movie) {
                                    ((Movie) movie).displayInfo();
                                }
                            }
                        } else {
                            System.out.println("No matching movies found.");
                        }
                        break;
                    }
                    case "series": {
                        System.out.println("Enter the title of the series you want to search:");
                        String seriesTitle = scanner.nextLine().toLowerCase();

                        List<Production> matchingSeries = new ArrayList<>();
                        for (Production series : productions) {
                            if (series instanceof Series && series.getProductionTitle().toLowerCase().contains(seriesTitle)) {
                                matchingSeries.add(series);
                            }
                        }

                        if (!matchingSeries.isEmpty()) {
                            System.out.println("Search result:\n");
                            for (Production series : matchingSeries) {
                                if (series instanceof Series) {
                                    ((Series) series).displayInfo();
                                }
                            }
                        } else {
                            System.out.println("No matching series found.");
                        }
                        break;
                    }
                    default: {
                        System.out.println(("Nothing to search for.\n"));
                        break;
                    }
                }
                break;
            }
            case 5: {
                // Handle Add/Delete actor/movie/series to/from favorites
                user.printFavoritesList();
                System.out.println("1) Add to favorites");
                System.out.println("2) Delete from favorites");
                System.out.println("3) Go back");

                Scanner scanner = new Scanner(System.in);
                int addOrDeleteChoice = scanner.nextInt();
                scanner.nextLine();


                switch (addOrDeleteChoice) {
                    case 1: {
                        // Add to favorites
                        user.addFavorite(productions, actors);
                        break;
                    }
                    case 2: {
                        // Delete from favorites
                        user.removeFavorite();
                        break;
                    }
                    default: {
                        System.out.println("Back to menu...");
                        break;
                    }
                }
                break;
            }
            case 6: {
                if (user instanceof Regular) {
                    // Handle Add/Delete review
                    System.out.println("1) Add review");
                    System.out.println("2) Delete review");
                    System.out.println("3) Go back");

                    Scanner scanner = new Scanner(System.in);
                    int addOrDeleteChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (addOrDeleteChoice) {
                        case 1: {
                            // Add review
                            Regular regular = new Regular();
                            regular.addReview(user);
                            break;
                        }
                        case 2: {
                            // Delete review
                            Regular regular = new Regular();
                            regular.removeReview(user);
                            break;
                        }
                        default: {
                            System.out.println("Back to menu...");
                            break;
                        }
                    }
                } else if (user instanceof Contributor) {
                    // Handle Add/Delete actor/movie/series from system
                    System.out.println("1) Add actor/movie/series");
                    System.out.println("2) Delete actor/movie/series");
                    System.out.println("3) Go back");

                    Scanner scanner = new Scanner(System.in);
                    int addOrDeleteChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (addOrDeleteChoice) {
                        case 1: {
                            // Add to system
                            System.out.println("What do you want to add? (actor/production)");
                            String inputChoice = scanner.nextLine().toLowerCase();

                            switch (inputChoice) {
                                case "actor": {
                                    Contributor contributor = new Contributor();
                                    contributor.addActorSystem(actors);
                                    break;
                                }
                                case "production": {
                                    Contributor contributor = new Contributor();
                                    contributor.addProductionSystem(productions);
                                    break;
                                }
                                default: {
                                    System.out.println("Back to menu...");
                                    break;
                                }
                            }
                            break;
                        }
                        case 2: {
                            // Delete from system
                            System.out.println("What do you want to delete? (actor/production)");
                            String inputChoice = scanner.nextLine().toLowerCase();

                            switch (inputChoice) {
                                case "actor": {
                                    Contributor contributor = new Contributor();
                                    contributor.removeActorsystem(actors);
                                    break;
                                }
                                case "production": {
                                    Contributor contributor = new Contributor();
                                    contributor.removeProductionSystem(productions);
                                    break;
                                }
                                default: {
                                    System.out.println("Back to menu...");
                                    break;
                                }
                            }
                            break;
                        }
                        default: {
                            System.out.println("Back to menu...");
                            break;
                        }
                    }
                    break;
                } else if (user instanceof Admin) {
                    // Handle Add/Delete user
                    System.out.println("1) Add user");
                    System.out.println("2) Remove user");
                    System.out.println("3) Go back");

                    Scanner scanner = new Scanner(System.in);
                    int addOrDeleteChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (addOrDeleteChoice) {
                        case 1: {
                            // Add user
                            Admin admin = new Admin();
                            admin.addUser(user, users);
                            break;
                        }
                        case 2: {
                            // Delete user
                            Admin admin = new Admin();
                            admin.removeUser(users);
                            break;
                        }
                        default: {
                            System.out.println("Back to menu...");
                            break;
                        }
                    }
                }
                break;
            }
            case 7: {
                if (user instanceof Regular) {
                    // Handle Add/Delete a request
                    System.out.println("1) Add request");
                    System.out.println("2) Delete request");
                    System.out.println("3) Go back");

                    Scanner scanner = new Scanner(System.in);
                    int addOrDeleteChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (addOrDeleteChoice) {
                        case 1: {
                            // Add request
                            Regular regular = new Regular();
                            regular.createRequest(requests, user);

                            for (Request request : requests) {
                                request.displayInfo();
                            }
                            break;
                        }
                        case 2: {
                            // Delete request
                            Regular regular = new Regular();
                            regular.removeRequest(requests, user);

                            for (Request request : requests) {
                                request.displayInfo();
                            }
                            break;
                        }
                        default: {
                            System.out.println("Back to menu...");
                            break;
                        }
                    }
                } else if (user instanceof Contributor) {
                    // Handle Update Movie Details
                    Contributor contributor = new Contributor();
                    contributor.updateProduction(productions);
                } else if (user instanceof Admin) {
                    // Handle Add/Delete actor/movie/series from system
                    System.out.println("1) Add actor/movie/series");
                    System.out.println("2) Delete actor/movie/series");
                    System.out.println("3) Go back");

                    Scanner scanner = new Scanner(System.in);
                    int addOrDeleteChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (addOrDeleteChoice) {
                        case 1: {
                            System.out.println("What do you want to add? (actor/production)");
                            String inputChoice = scanner.nextLine().toLowerCase();

                            switch (inputChoice) {
                                case "actor": {
                                    Admin admin = new Admin();
                                    admin.addActorSystem(actors);
                                    break;
                                }
                                case "production": {
                                    Admin admin = new Admin();
                                    admin.addProductionSystem(productions);
                                    break;
                                }
                                default: {
                                    System.out.println("Back to menu...");
                                    break;
                                }
                            }
                            break;
                        }
                        case 2: {
                            System.out.println("What do you want to delete? (actor/production)");
                            String inputChoice = scanner.nextLine().toLowerCase();

                            switch (inputChoice) {
                                case "actor": {
                                    Admin admin = new Admin();
                                    admin.removeActorsystem(actors);
                                    break;
                                }
                                case "production": {
                                    Admin admin = new Admin();
                                    admin.removeProductionSystem(productions);
                                    break;
                                }
                                default: {
                                    break;
                                }
                            }
                            break;
                        }
                        default: {
                            System.out.println("Back to menu...");
                            break;
                        }
                    }
                }
                break;
            }
            case 8: {
                if (user instanceof Contributor) {
                    // Handle Update Actor Details
                    Contributor contributor = new Contributor();
                    contributor.updateActor(actors);
                } else if (user instanceof Admin) {
                    // Handle Update Movie Details
                    Admin admin = new Admin();
                    admin.updateProduction(productions);
                }
                break;
            }
            case 9: {
                if (user instanceof Contributor) {
                    // Handle Solve a request
                    for (Request request : requests) {
                        request.displayInfo();
                    }
                } else if (user instanceof Admin) {
                    // Handle Update Actor Details
                    Admin admin = new Admin();
                    admin.updateActor(actors);
                }
                break;
            }
            case 10: {
                if (user instanceof Contributor) {
                    //Handle Add/Delete a request
                    System.out.println("1) Add request");
                    System.out.println("2) Delete request");
                    System.out.println("3) Go back");

                    Scanner scanner = new Scanner(System.in);
                    int addOrDeleteChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (addOrDeleteChoice) {
                        case 1: {
                            // Add request
                            Contributor contributor = new Contributor();
                            contributor.createRequest(requests, user);

                            for (Request request : requests) {
                                request.displayInfo();
                            }
                            break;
                        }
                        case 2: {
                            // Delete request
                            Contributor contributor = new Contributor();
                            contributor.removeRequest(requests, user);

                            for (Request request : requests) {
                                request.displayInfo();
                            }
                            break;
                        }
                        default: {
                            System.out.println("Back to menu...");
                            break;
                        }
                    }
                } else if (user instanceof Admin) {
                    // Handle Solve a request for Admin
                    for (Request request : requests) {
                        request.displayInfo();
                    }
                    break;
                }
                break;
            }
            default: {
                System.out.println("Invalid choice.");
                break;
            }
        }
    }

    public static Production findProductionByTitle(String title) {
        for (Production production : productions) {
            if (production.getProductionTitle().toLowerCase().equals(title)) {
                return production;
            }
        }
        return null;
    }

    public static User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}

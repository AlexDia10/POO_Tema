package org.example;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class User implements Comparable<User>, Observer {
    private Information information;
    private AccountType type;
    private String username;
    private int experience;
    private List<String> notifications;
    private SortedSet<Object> favoritesList = new TreeSet<>();

    // Null constructor
    public User() {
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    public void setFavoritesList(SortedSet<Object> favoritesList) {
        this.favoritesList = favoritesList;
    }

    public Information getInformation() {
        return information;
    }

    public AccountType getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public int getExperience() {
        return experience;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public SortedSet<Object> getFavoritesList() {
        return favoritesList;
    }

    public Credentials getCredentials() {
        if (information != null) {
            return information.getCredentials();
        } else {
            return null;
        }
    }

    // Check credentials for authentication
    public boolean isValidCredentials(String enteredEmail, String enteredPassword) {
        Credentials credentials = getCredentials();

        // Check if credentials is not null
        return credentials != null &&
                credentials.getEmail().equals(enteredEmail) &&
                credentials.getPassword().equals(enteredPassword);
    }

    // Handle notifications
    public void printNotifications() {
        if (notifications != null && !notifications.isEmpty()) {
            System.out.println("Notifications:");
            for (String notification : notifications) {
                System.out.println(notification);
            }
        } else {
            System.out.println("No notifications.");
        }
    }

    public void addNotification(String notification) {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        notifications.add(notification);
    }

    // Favorite list functions
    public void addFavorite(List<Production> productions, List<Actor> actors) {
        System.out.println("Enter the name of the actor/movie/series to add to favorites:");
        Scanner scanner = new Scanner(System.in);
        String favoriteToAdd = scanner.nextLine();

        int ok = 0;
        for (Production production : productions) {
            if (production.getProductionTitle().toLowerCase().contains(favoriteToAdd.toLowerCase())) {
                favoritesList.add(favoriteToAdd);
                System.out.println("Production " + favoriteToAdd + " added to favorites.");
                ok = 1;
            }
        }
        for (Actor actor : actors) {
            if (actor.getName().toLowerCase().contains(favoriteToAdd.toLowerCase())) {
                favoritesList.add(favoriteToAdd);
                System.out.println("Actor " + favoriteToAdd + " added to favorites.");
                ok = 1;
            }
        }
        if (ok == 0) {
            System.out.println(favoriteToAdd + " is not a production nor an actor.");
        }
        printFavoritesList();
    }

    public void removeFavorite() {
        printFavoritesList();
        System.out.println("Enter the name of the actor/movie/series to delete from favorites:");
        Scanner scanner = new Scanner(System.in);
        String favoriteToDelete = scanner.nextLine();

        if (favoritesList.contains(favoriteToDelete)) {
            favoritesList.remove(favoriteToDelete);
            System.out.println(favoriteToDelete + " deleted from favorites.");
        } else {
            System.out.println(favoriteToDelete + " is not in the favorites list.");
        }
        printFavoritesList();
    }

    public void printFavoritesList() {
        if (favoritesList != null && !favoritesList.isEmpty()) {
            System.out.println("Favorites list:");
            for (Object favorite : favoritesList) {
                System.out.println(favorite);
            }
        } else {
            System.out.println("No favorites.");
        }
        System.out.println("");
    }

    // Information inner class
    public static class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private Gender gender;
        private LocalDate birthDate;

        private Information(InformationBuilder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
        }
        public static class InformationBuilder {
            private Credentials credentials;
            private String name;
            private String country;
            private Gender gender;
            private LocalDate birthDate;

            public InformationBuilder(String name, String country) {
                this.name = name;
                this.country = country;
            }
            public InformationBuilder credentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }
            public InformationBuilder gender(Gender gender) {
                this.gender = gender;
                return this;
            }
            public InformationBuilder birthDate(LocalDate birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public String getFormattedBirthDate() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return birthDate.format(formatter);
        }
    }

    //
    public abstract void updateExperience(int newExperience);

    public abstract void logout();

    //
    @Override
    public int compareTo(@NotNull User o) {
        return this.getUsername().compareTo(o.getUsername());
    }
}

package org.example;

import java.util.*;

public abstract class Staff extends User implements StaffInterface {
    private List<Request> assignedRequest;
    private SortedSet<Production> addedItems = new TreeSet<>();

    public Staff() {
    }

    public SortedSet<Production> getAddedItems() {
        return addedItems;
    }

    // Metodele din StaffInterface
    private static String getGenreList() {
        StringBuilder genres = new StringBuilder();
        for (Genre genre : Genre.values()) {
            genres.append(genre).append(", ");
        }
        return genres.substring(0, genres.length() - 2); // Remove the trailing comma and space
    }

    @Override
    public void addProductionSystem(List<Production> productions) {
        System.out.println("What do you want to add? (movie/series)");
        Scanner scanner = new Scanner(System.in);
        String inputChoice = scanner.nextLine().toLowerCase();

        switch (inputChoice) {
            case "movie": {
                System.out.println("Enter details for new movie:");

                System.out.print("Enter title: ");
                String title = scanner.nextLine();

                List<Genre> selectedGenres = new ArrayList<>();

                while (true) {
                    System.out.println("Available genres: " + getGenreList());
                    System.out.print("Enter genre one by one (or 'done' to finish): ");
                    String inputGenre = scanner.nextLine().trim();

                    if ("done".equalsIgnoreCase(inputGenre)) {
                        break;  // Exit the loop if the user enters 'done'
                    }

                    try {
                        Genre selectedGenre = Genre.valueOf(inputGenre);
                        selectedGenres.add(selectedGenre);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Please enter a valid genre from the list.");
                    }
                }

                System.out.print("Enter duration: ");
                String duration = scanner.nextLine();

                System.out.print("Enter release year: ");
                int release_year = scanner.nextInt();

                Movie newMovie = new Movie(title, selectedGenres, duration, release_year);
                productions.add(newMovie);
                //addedItems.add(newMovie);
                System.out.println("New movie added to the system.");

                break;
            }
            case "series": {
                System.out.println("Enter details for new series:");

                System.out.print("Enter title: ");
                String title = scanner.nextLine();

                List<Genre> selectedGenres = new ArrayList<>();

                while (true) {
                    System.out.println("Available genres: " + getGenreList());
                    System.out.print("Enter genre one by one (or 'done' to finish): ");
                    String inputGenre = scanner.nextLine().trim();

                    if ("done".equalsIgnoreCase(inputGenre)) {
                        break;  // Exit the loop if the user enters 'done'
                    }

                    try {
                        Genre selectedGenre = Genre.valueOf(inputGenre);
                        selectedGenres.add(selectedGenre);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Please enter a valid genre from the list.");
                    }
                }

                System.out.print("Enter release year: ");
                int releaseYear = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Enter number of seasons: ");
                int seasonsNumber = scanner.nextInt();
                scanner.nextLine();

                // Add seasons and episodes
                Map<String, List<Episode>> episodeList = new HashMap<>();

                for (int seasonNumber = 1; seasonNumber <= seasonsNumber; seasonNumber++) {
                    String seasonName = "Season " + seasonNumber;
                    System.out.println("Enter details for " + seasonName + ":");

                    List<Episode> episodes = new ArrayList<>();

                    System.out.print("Enter number of episodes: ");
                    int episodesNumber = scanner.nextInt();
                    scanner.nextLine();

                    for (int episodeNumber = 1; episodeNumber <= episodesNumber; episodeNumber++) {
                        System.out.print("Enter episode name for " + seasonName + ", Episode " + episodeNumber + ": ");
                        String episodeName = scanner.nextLine();

                        System.out.print("Enter episode duration for " + seasonName + ", Episode " + episodeNumber + ": ");
                        String episodeDuration = scanner.nextLine();

                        Episode episode = new Episode(episodeName, episodeDuration);
                        episodes.add(episode);
                    }
                    episodeList.put(seasonName, episodes);
                }

                Series newSeries = new Series(title, selectedGenres, releaseYear, seasonsNumber, episodeList);
                productions.add(newSeries);
                //addedItems.add(newSeries);
                System.out.println("New series added to the system.");

                break;
            }
            default: {
                System.out.println("Invalid choice.");
                break;
            }
        }
    }

    @Override
    public void addActorSystem(List<Actor> actors) {
        System.out.println("Enter details for the new actor:");

        System.out.print("Enter name: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        System.out.print("Enter biography: ");
        String biography = scanner.nextLine();

        Actor newActor = new Actor(name, biography);
        actors.add(newActor);
        System.out.println("New actor added to the system.");
    }

    @Override
    public void removeProductionSystem(List<Production> productions) {
        System.out.println("What do you want to delete? (movie/series)");
        Scanner scanner = new Scanner(System.in);
        String inputChoice = scanner.nextLine().toLowerCase();

        switch (inputChoice) {
            case "movie": {
                System.out.println("List of Movies:");
                List<Production> movieList = new ArrayList<>();
                int index = 1;
                for (Production production : productions) {
                    if (production instanceof Movie) {
                        System.out.println(index + ") " + production.getProductionTitle());
                        movieList.add(production);
                        index++;
                    }
                }

                System.out.print("Enter the number of the movie to delete: ");
                int movieIndexToDelete = scanner.nextInt();

                if (movieIndexToDelete >= 1 && movieIndexToDelete < index) {
                    Production productionToDelete = movieList.get(movieIndexToDelete - 1);
                    String movieTitleToDelete = productionToDelete.getProductionTitle();

                    // Remove the movie from the productions list using its title
                    productions.removeIf(production -> production.getProductionTitle().equals(movieTitleToDelete));
                    addedItems.removeIf(item -> item instanceof Movie && ((Movie) item).getProductionTitle().equals(movieTitleToDelete));

                    System.out.println("Movie " + productionToDelete.getProductionTitle() + " deleted.");

                    System.out.println("List of Movies:");
                    index = 1;
                    for (Production production : productions) {
                        if (production instanceof Movie) {
                            System.out.println(index++ + ") " + production.getProductionTitle());
                        }
                    }

                } else {
                    System.out.println("Invalid movie index.");
                }
                break;
            }
            case "series": {
                System.out.println("List of Series:");
                List<Production> seriesList = new ArrayList<>();
                int index = 1;
                for (Production production : productions) {
                    if (production instanceof Series) {
                        System.out.println(index + ") " + production.getProductionTitle());
                        seriesList.add(production);
                        index++;
                    }
                }

                System.out.print("Enter the number of the series to delete: ");
                int seriesIndexToDelete = scanner.nextInt();

                if (seriesIndexToDelete >= 1 && seriesIndexToDelete < index) {
                    Production productionToDelete = seriesList.get(seriesIndexToDelete - 1);
                    String seriesTitleToDelete = productionToDelete.getProductionTitle();

                    // Remove the series from the productions list using its title
                    productions.removeIf(production -> production.getProductionTitle().equals(seriesTitleToDelete));
                    addedItems.removeIf(item -> item instanceof Series && ((Series) item).getProductionTitle().equals(seriesTitleToDelete));

                    System.out.println("Series " + productionToDelete.getProductionTitle() + " deleted.");

                    System.out.println("List of Series:");
                    index = 1;
                    for (Production production : productions) {
                        if (production instanceof Series) {
                            System.out.println(index++ + ") " + production.getProductionTitle());
                        }
                    }

                } else {
                    System.out.println("Invalid series index.");
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void removeActorsystem(List<Actor> actors) {
        System.out.println("List of Actors:");
        for (int i = 0; i < actors.size(); i++) {
            System.out.println((i + 1) + ". " + actors.get(i).getName());
        }

        System.out.print("Enter the number of the actor to delete: ");
        Scanner scanner = new Scanner(System.in);
        int actorIndexToDelete = scanner.nextInt();

        if (actorIndexToDelete >= 1 && actorIndexToDelete <= actors.size()) {
            Actor actorToDelete = actors.remove(actorIndexToDelete - 1);
            System.out.println("Actor " + actorToDelete.getName() + " deleted.");

            for (int i = 0; i < actors.size(); i++) {
                System.out.println((i + 1) + ". " + actors.get(i).getName());
            }
        } else {
            System.out.println("Invalid actor index.");
        }
    }

    @Override
    public void updateProduction(List<Production> productions) {
        System.out.println("List of Productions:");
        for (int i = 0; i < productions.size(); i++) {
            System.out.println((i + 1) + ". " + productions.get(i).getProductionTitle());
        }
        System.out.print("Enter the number of the production to update: ");
        Scanner scanner = new Scanner(System.in);
        int productionIndexToUpdate = scanner.nextInt();

        if (productionIndexToUpdate >= 1 && productionIndexToUpdate <= productions.size()) {
            Production productionToUpdate = productions.get(productionIndexToUpdate - 1);

            System.out.println("Current information about " + productionToUpdate.getProductionTitle() + ":");
            productionToUpdate.displayInfo();

            System.out.println("Choose what to update for " + productionToUpdate.getProductionTitle() + ":");
            System.out.println("1) Update Title");
            System.out.println("2) Update Genre");
            System.out.println("3) Update Duration for Movie");
            System.out.println("4) Update Release Year");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: {
                    System.out.print("Enter new title: ");
                    String newTitle = scanner.nextLine();
                    productionToUpdate.setProductionTitle(newTitle);
                    System.out.println("Name updated successfully.");
                    break;
                }
                case 2: {

                    System.out.println("Genre updated successfully.");
                    break;
                }
                case 3: {
                    if (productionToUpdate instanceof Movie) {
                        System.out.print("Enter new duration: ");
                        String movieDuration = scanner.nextLine();
                        ((Movie) productionToUpdate).setMovieDuration(movieDuration);
                        System.out.println("Duration updated successfully.");
                    }
                    break;
                }
                case 4: {
                    if (productionToUpdate instanceof Movie) {
                        System.out.println("Enter new release year: ");
                        int newReleaseYear = scanner.nextInt();
                        scanner.nextLine();
                        ((Movie) productionToUpdate).setReleaseMovieYear(newReleaseYear);
                    } else if (productionToUpdate instanceof Series) {
                        System.out.println("Enter new release year: ");
                        int newReleaseYear = scanner.nextInt();
                        scanner.nextLine();
                        ((Series) productionToUpdate).setReleaseSeriesYear(newReleaseYear);
                    }
                    System.out.println("Release year updated successfully.");
                    break;
                }
                default: {
                    System.out.println("Invalid choice. No updates made.");
                    break;
                }
            }
            System.out.println("Production details updated successfully.");
        } else {
            System.out.println("Invalid production index. No changes made.");
        }
    }

    @Override
    public void updateActor(List<Actor> actors) {
        System.out.println("List of Actors:");
        for (int i = 0; i < actors.size(); i++) {
            System.out.println((i + 1) + ". " + actors.get(i).getName());
        }
        System.out.print("Enter the number of the actor to update: ");
        Scanner scanner = new Scanner(System.in);
        int actorIndexToUpdate = scanner.nextInt();

        if (actorIndexToUpdate >= 1 && actorIndexToUpdate <= actors.size()) {
            Actor actorToUpdate = actors.get(actorIndexToUpdate - 1);

            System.out.println("Current information about " + actorToUpdate.getName() + ":");
            actorToUpdate.displayInfo();

            System.out.println("Choose what to update for " + actorToUpdate.getName() + ":");
            System.out.println("1) Update Name");
            System.out.println("2) Update Biography");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: {
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    actorToUpdate.setActorName(newName);
                    System.out.println("Name updated successfully.");
                    break;
                }
                case 2: {
                    System.out.print("Enter new biography: ");
                    String newBiography = scanner.nextLine();
                    actorToUpdate.setBiography(newBiography);
                    System.out.println("Biography updated successfully.");
                    break;
                }
                default: {
                    System.out.println("Invalid choice. No updates made.");
                    break;
                }
            }
            System.out.println("Actor details updated successfully.");
        } else {
            System.out.println("Invalid actor index. No changes made.");
        }
    }

    // Metodele din superclasa User
    @Override
    public void updateExperience(int newExperience) {

    }

    @Override
    public void logout() {

    }

    // Metode specifica clasei Staff
    public void resolveRequests(List<Request> requests) {

    }
}

package org.example;

import java.util.*;

import static org.example.IMDB.*;

public abstract class Production implements Comparable<Production> {
    private String productionTitle;
    private List<String> directorsName;
    private List<String> actorsName;
    private List<Genre> genreList;
    private List<Rating> ratingList;
    private String plot;
    private Double averageRating;

    private List<User> observers = new ArrayList<>();

    public String getProductionTitle() {
        return productionTitle;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }

    public List<Rating> getRatingList() {
        return ratingList;
    }

    public void setProductionTitle(String productionTitle) {
        this.productionTitle = productionTitle;
    }

    public Production(String productionTitle, List<Genre> genreList) {
        this.productionTitle = productionTitle;
        this.genreList = genreList;
    }

    public Production(String productionTitle, List<String> directorsName, List<String> actorsName, List<Genre> genreList, List<Rating> ratingList, String moviePlot, Double movieGrade) {
        this.productionTitle = productionTitle;
        this.directorsName = directorsName;
        this.actorsName = actorsName;
        this.genreList = genreList;
        this.ratingList = ratingList;
        this.plot = moviePlot;
        this.averageRating = movieGrade;
    }

    public abstract void displayInfo();

    public int compareTo(Production other) {
        return this.productionTitle.compareTo(other.productionTitle);
    }

    public Rating findReviewByUser(User user) {
        for (Rating rating : ratingList) {
            if (rating.getUsername().equals(user.getUsername())) {
                return rating;
            }
        }
        return null;
    }

    public void addReview(Rating newRating) {
        ratingList.add(newRating);
    }

    public void removeReview(User user) {
        Rating rating = findReviewByUser(user);
        if (rating != null) {
            ratingList.remove(rating);
        }
    }

    // Observer pattern
    public void addObserver(User observer) {
        observers.add(observer);
    }

    public void removeObserver(User observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Rating newRating, Production ratedProduction) {
        for (Rating rating : ratingList) {
            String username = rating.getUsername();
            User user = getUserByUsername(username);
            observers.add(user);
        }

        /*List<User> users = getUsers();
        for (User user : users) {
            if (user instanceof Staff) {
                Staff staff = (Staff) user;
                SortedSet<Production> addedItems = staff.getAddedItems();

                if (addedItems.contains(ratedProduction)) {
                    observers.add(user);
                }
            }
        }*/

        for (User observer : observers) {
            //System.out.println(observer.getUsername());
            observer.update(this, newRating, observer);
        }
    }
}

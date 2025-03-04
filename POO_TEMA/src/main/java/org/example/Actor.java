package org.example;

import java.util.Map;

public class Actor {
    private String actorName;
    private Map<String, Type> performances;
    private String biography;

    public Actor(String actorName, String biography) {
        this.actorName = actorName;
        this.biography = biography;
    }

    public Actor(String actorName, Map<String, Type> performances, String biography) {
        this.actorName = actorName;
        this.performances = performances;
        this.biography = biography;
    }

    public String getName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void displayInfo() {
        System.out.println("Actor name: " + actorName);
        if (biography != null) {
            System.out.println("Biography: " + biography);
        }
        System.out.println("");
    }
}

package org.example;

import java.util.List;

interface RequestManager {
    public void createRequest(List<Request> requests, User user);
    public void removeRequest(List<Request> requests, User user);
}

interface StaffInterface {
    public void addProductionSystem(List<Production> productions);
    public void addActorSystem(List<Actor> actors);
    public void removeProductionSystem(List<Production> productions);
    public void removeActorsystem(List<Actor> actors);
    public void updateProduction(List<Production> productions);
    public void updateActor(List<Actor> actors);
}

interface Observer {
    public void update(Production production, Rating rating, User observer);

    public void update(String notification);
}

interface ExperienceStrategy {
    public int calculateExperience();
}

interface Comparable<T> {
    int compareTo(T o);
}

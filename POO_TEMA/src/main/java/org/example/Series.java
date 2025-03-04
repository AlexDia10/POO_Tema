package org.example;

import java.util.*;

public class Series extends Production {
    private int releaseSeriesYear;
    private int seasonsNumber;
    private Map<String, List<Episode>> episodeList;

    public void setReleaseSeriesYear(int releaseSeriesYear) {
        this.releaseSeriesYear = releaseSeriesYear;
    }

    public Series(String productionTitle, List<Genre> genreList, int releaseSeriesYear, int seasonsNumber, Map<String, List<Episode>> episodeList) {
        super(productionTitle, genreList);
        this.releaseSeriesYear = releaseSeriesYear;
        this.seasonsNumber = seasonsNumber;
        this.episodeList = episodeList;
    }

    public Series(String productionTitle, List<String> directorsName, List<String> actorsName, List<Genre> genreList, List<Rating> ratingList, String moviePlot, Double movieGrade, int releaseSeriesYear, int seasonsNumber, Map<String, List<Episode>> seasonEpisodes) {
        super(productionTitle, directorsName, actorsName, genreList, ratingList, moviePlot, movieGrade);
        this.releaseSeriesYear = releaseSeriesYear;
        this.seasonsNumber = seasonsNumber;
        this.episodeList = seasonEpisodes;

    }

    @Override
    public void displayInfo() {
        System.out.println("Serie title: " + getProductionTitle());
        System.out.println("Serie genre: " + getGenreList());
        System.out.println("Serie release year: " + releaseSeriesYear);

        List<Rating> ratings = getRatingList();
        System.out.println("Number of ratings: " + (ratings != null ? ratings.size() : 0));

        System.out.println("Number of seasons: " + seasonsNumber);
        System.out.println("Seasons: ");
        for (int i = 1; i <= seasonsNumber; i++) {
            System.out.println("Season " + i);
            List<Episode> episodes = episodeList.get("Season " + i);
            for (Episode episode : episodes) {
                System.out.println("Episode name: " + episode.getEpisodeName());
                System.out.println("Episode duration: " + episode.getEpisodeDuration());
            }
        }
        System.out.println("");
    }
}

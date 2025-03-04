package org.example;

import java.util.List;

public class Movie extends Production {
    private String movieDuration;
    private int releaseMovieYear;

    public void setReleaseMovieYear(int releaseMovieYear) {
        this.releaseMovieYear = releaseMovieYear;
    }

    public void setMovieDuration(String movieDuration) {
        this.movieDuration = movieDuration;
    }

    public Movie(String productionTitle, List<Genre> genreList, String movieDuration, int releaseMovieYear) {
        super(productionTitle, genreList);
        this.movieDuration = movieDuration;
        this.releaseMovieYear = releaseMovieYear;
    }

    public Movie(String productionTitle, List<String> directorsName, List<String> actorsName, List<Genre> genreList, List<Rating> ratingList, String moviePlot, Double movieGrade, String movieDuration, int releaseMovieYear) {
        super(productionTitle, directorsName, actorsName, genreList, ratingList, moviePlot, movieGrade);
        this.movieDuration = movieDuration;
        this.releaseMovieYear = releaseMovieYear;
    }

    @Override
    public void displayInfo() {
        System.out.println("Movie title: " + (getProductionTitle() != null ? getProductionTitle() : "Unknown"));
        System.out.println("Movie genre: " + (getGenreList() != null ? getGenreList() : "Unknown"));
        System.out.println("Movie duration: " + (movieDuration != null ? movieDuration : "Unknown"));
        System.out.println("Movie release year: " + (releaseMovieYear != 0 ? releaseMovieYear : "Unknown"));

        List<Rating> ratings = getRatingList();
        System.out.println("Number of ratings: " + (ratings != null ? ratings.size() : 0));

        System.out.println("");
    }
}

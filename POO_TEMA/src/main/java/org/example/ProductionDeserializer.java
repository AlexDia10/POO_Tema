package org.example;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.*;

public class ProductionDeserializer extends StdDeserializer<Production> {

    public ProductionDeserializer() {
        this(null);
    }

    protected ProductionDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Production deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        String type = node.get("type").asText();

        switch (type) {
            case "Movie":
                return deserializeMovie(node);
            case "Series":
                return deserializeSeries(node);
            default:
                throw new IllegalArgumentException("Unknown production type: " + type);
        }
    }

    private Movie deserializeMovie(JsonNode node) {
        String title = node.get("title").asText();
        List<String> directors = getListFromNode(node, "directors");
        List<String> actors = getListFromNode(node, "actors");
        List<Genre> genres = getGenreListFromNode(node, "genres");

        List<Rating> ratings = deserializeRatings(node.path("ratings"));

        String plot = node.get("plot").asText();
        double averageRating = node.get("averageRating").asDouble();
        String duration = node.get("duration").asText();

        JsonNode releaseYearNode = node.get("releaseYear");
        int releaseYear = releaseYearNode != null ? releaseYearNode.asInt() : 0;

        return new Movie(title, directors, actors, genres, ratings, plot, averageRating, duration, releaseYear);
    }

    private Series deserializeSeries(JsonNode node) {
        String title = node.get("title").asText();
        List<String> directors = getListFromNode(node, "directors");
        List<String> actors = getListFromNode(node, "actors");
        List<Genre> genres = getGenreListFromNode(node, "genres");

        List<Rating> ratings = deserializeRatings(node.path("ratings"));

        String plot = node.get("plot").asText();
        double averageRating = node.get("averageRating").asDouble();

        JsonNode releaseYearNode = node.get("releaseYear");
        int releaseYear = releaseYearNode != null ? releaseYearNode.asInt() : 0;

        int numSeasons = node.get("numSeasons").asInt();

        Map<String, List<Episode>> seasons = deserializeSeasons(node.path("seasons"));

        return new Series(title, directors, actors, genres, ratings, plot, averageRating, releaseYear, numSeasons, seasons);
    }

    private List<Rating> deserializeRatings(JsonNode ratingsNode) {
        List<Rating> ratings = new ArrayList<>();
        for (JsonNode ratingNode : ratingsNode) {
            String username = ratingNode.get("username").asText();
            int ratingValue = ratingNode.get("rating").asInt();
            String comment = ratingNode.get("comment").asText();
            ratings.add(new Rating(username, ratingValue, comment));
        }
        return ratings;
    }

    private Map<String, List<Episode>> deserializeSeasons(JsonNode seasonsNode) {
        Map<String, List<Episode>> seasonEpisodes = new HashMap<>();

        for (Iterator<String> it = seasonsNode.fieldNames(); it.hasNext(); ) {
            String seasonName = it.next();
            JsonNode episodesNode = seasonsNode.get(seasonName);
            List<Episode> episodeList = new ArrayList<>();

            for (JsonNode episodeNode : episodesNode) {
                Episode episode = deserializeEpisode(episodeNode);
                episodeList.add(episode);
            }

            seasonEpisodes.put(seasonName, episodeList);
        }

        return seasonEpisodes;
    }

    private static Episode deserializeEpisode(JsonNode episodeNode) {
        String episodeName = episodeNode.get("episodeName").asText();
        String duration = episodeNode.get("duration").asText();
        return new Episode(episodeName, duration);
    }

    private List<String> getListFromNode(JsonNode node, String fieldName) {
        List<String> list = new ArrayList<>();
        for (JsonNode elementNode : node.path(fieldName)) {
            list.add(elementNode.asText());
        }
        return list;
    }

    private List<Genre> getGenreListFromNode(JsonNode node, String fieldName) {
        List<Genre> genreList = new ArrayList<>();
        for (JsonNode genreNode : node.path(fieldName)) {
            String genreName = genreNode.asText();
            Genre genre = Genre.valueOf(genreName); // Assuming Genre is an enum
            genreList.add(genre);
        }
        return genreList;
    }
}


package org.example;

public class Episode {
    private String episodeName;
    private String episodeDuration;

    public Episode(String episodeName, String episodeDuration) {
        this.episodeName = episodeName;
        this.episodeDuration = episodeDuration;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public String getEpisodeDuration() {
        return episodeDuration;
    }
}

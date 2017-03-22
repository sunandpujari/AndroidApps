package edu.uncc.thegamedb;

import java.util.Date;

/**
 * Created by NANDU on 13-02-2017.
 */

public class Game {

    private int id;
    private String gameTitle;
    private String releaseDate;
    private String platform;
    private String overview;
    private String genre;
    private String youtubeLink;
    private String similarGameIds;
    private String baseImageUrl;
    private String boxart;
    private String clearlogo;

    public String getClearlogo() {
        return clearlogo;
    }

    public void setClearlogo(String clearlogo) {
        this.clearlogo = clearlogo;
    }

    @Override
    public String toString() {
        return "Game{" +
                "baseImageUrl='" + baseImageUrl + '\'' +
                ", id=" + id +
                ", gameTitle='" + gameTitle + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", platform='" + platform + '\'' +
                ", overview='" + overview + '\'' +
                ", genre='" + genre + '\'' +
                ", youtubeLink='" + youtubeLink + '\'' +
                ", similarGameIds='" + similarGameIds + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", publisher='" + publisher + '\'' +
                ", boxart='" + boxart + '\'' +
                '}';
    }

    public String getBoxart() {
        return boxart;
    }

    public void setBoxart(String boxart) {
        this.boxart = boxart;
    }

    private String imagePath;
    private String publisher;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getBaseImageUrl() {
        return baseImageUrl;
    }

    public void setBaseImageUrl(String baseImageUrl) {
        this.baseImageUrl = baseImageUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getSimilarGameIds() {
        return similarGameIds;
    }

    public void setSimilarGameIds(String similarGameIds) {
        this.similarGameIds = similarGameIds;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}

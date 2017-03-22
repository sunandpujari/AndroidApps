package edu.uncc.cnnnews;

/**
 * Created by NANDU on 13-02-2017.
 */

public class News {
    @Override
    public String toString() {
        return "News{" +
                "description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                '}';
    }

    private String title;
    private String description;
    private String urlToImage;
    private String publishedAt;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}

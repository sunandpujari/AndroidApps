package edu.uncc.tedradiohourpodcast;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by NANDU on 06-03-2017.
 */

public class Pod implements Serializable {
    String title,imageURL,description,duration,mediaLink;
    Date publicationDate;

    @Override
    public String toString() {
        return "Pod{" +
                "description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", duration='" + duration + '\'' +
                ", mediaLink='" + mediaLink + '\'' +
                ", publicationDate=" + publicationDate +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public void setMediaLink(String mediaLink) {
        this.mediaLink = mediaLink;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

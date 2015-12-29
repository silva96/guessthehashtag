package com.silva.benjamin.guessthehashtag.models;

/**
 * Created by benjamin on 12/7/15.
 */
public class Media {
    private float distance;
    private String type;
    private String[] tags;
    private Images images;

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }
}

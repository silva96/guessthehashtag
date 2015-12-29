package com.silva.benjamin.guessthehashtag.models;

/**
 * Created by benjamin on 12/7/15.
 */
public class Images {
    private Image low_resolution;
    private Image thumbnail;
    private Image standard_resolution;

    public Image getLow_resolution() {
        return low_resolution;
    }

    public void setLow_resolution(Image low_resolution) {
        this.low_resolution = low_resolution;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Image getStandard_resolution() {
        return standard_resolution;
    }

    public void setStandard_resolution(Image standard_resolution) {
        this.standard_resolution = standard_resolution;
    }
}

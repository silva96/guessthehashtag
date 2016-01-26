package com.silva.benjamin.guessthehashtag.models;

import java.util.ArrayList;

/**
 * Created by benjamin on 12/7/15.
 */
public class SearchData extends Data {
    private Media[] data;

    public Media[] getData() {
        return data;
    }

    public void setData(Media[] data) {
        this.data = data;
    }

    public ArrayList<Media> filterMedia(String type) {
        ArrayList<Media> filtered = new ArrayList<>();
        for (Media m : data) {
            if (m.getType().equals(type)) filtered.add(m);
        }
        return filtered;
    }
}

package com.undot.appenginelesson.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 0503337710 on 06/06/2016.
 */
public class BaseResponse<T> {
    private List<T> items;
    private String nextPageToken;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
}

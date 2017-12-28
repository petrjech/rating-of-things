package com.jp.apps.rating_of_things;

import java.util.Date;

public class Rating {

    private int rating;
    private String note;
    private Date createTime;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

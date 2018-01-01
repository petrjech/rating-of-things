package com.jp.apps.rating_of_things;

import android.content.Intent;

import java.util.List;

public class Item {

    private long id;
    private String name;
    private List<Rating> ratings;
    private List<String> tags;
    private String description = "";

    public Item (String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void populateIntent(Intent intent) {
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("description", description);
    }

    public static Item createFromIntent(Intent intent) {
        Item item = new Item(intent.getStringExtra("name"));
        item.setId(intent.getIntExtra("id", 0));
        item.setDescription(intent.getStringExtra("description"));
        return item;
    }
}

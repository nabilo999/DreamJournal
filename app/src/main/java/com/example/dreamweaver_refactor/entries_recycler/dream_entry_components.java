package com.example.dreamweaver_refactor.entries_recycler;

public class dream_entry_components {

    private String title;
    private String date;
    private String tag;
    private String description;

    public dream_entry_components(){}// Default constructor required for Firebase
    public dream_entry_components(String title, String date, String tag, String description) {
        this.title = title;
        this.date = date;
        this.tag = tag;
        this.description =description;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

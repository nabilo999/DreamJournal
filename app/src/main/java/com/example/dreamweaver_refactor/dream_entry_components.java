package com.example.dreamweaver_refactor;

public class dream_entry_components {

    private String title;
    private String date;
    private String tag;

    public dream_entry_components(){}// Default constructor required for Firebase
    public dream_entry_components(String title, String date, String tag) {
        this.title = title;
        this.date = date;
        this.tag = tag;
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
}

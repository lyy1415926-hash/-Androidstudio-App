package com.example.campusinfoapp.data.model;

import com.google.gson.annotations.SerializedName;

public class News {

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String content;

    @SerializedName("urlToImage")
    private String imageUrl;

    private boolean isFavorite = false;
    private boolean isRead = false;

    public News() {}

    public News(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getImageUrl() { return imageUrl; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public void toggleFavorite() { isFavorite = !isFavorite; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}

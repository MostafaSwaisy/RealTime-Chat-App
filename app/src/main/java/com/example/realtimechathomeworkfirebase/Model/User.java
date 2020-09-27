package com.example.realtimechathomeworkfirebase.Model;

public class User {
    String username;
    String id;
    String imageUrl = "default";
    private String status;
    private String search;

    public User(String username, String id, String imageUrl, String status, String search) {
        this.username = username;
        this.id = id;
        this.imageUrl = imageUrl;
        this.search = search;
    }

    public User() {
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

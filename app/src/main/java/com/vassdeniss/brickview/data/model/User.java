package com.vassdeniss.brickview.data.model;

public class User {
    private String _id;
    private String username;
    private String email;
    private String image;
    private Set[] sets;

    public String getUsername() {
        return this.username;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

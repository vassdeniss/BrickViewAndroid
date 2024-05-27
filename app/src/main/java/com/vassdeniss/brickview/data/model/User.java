package com.vassdeniss.brickview.data.model;

public class User {
    private String _id;
    private String username;
    private String email;
    private String image;
    private Set[] sets;
    private Tokens tokens;

    public String getUsername() {
        return this.username;
    }

    public String getImage() {
        return this.image;
    }

    public Tokens getTokens() {
        return this.tokens;
    }

    public Set[] getSets() {
        return this.sets;
    }

    public void setTokens(Tokens tokens) {
        this.tokens = tokens;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

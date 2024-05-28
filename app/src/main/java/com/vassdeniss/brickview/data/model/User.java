package com.vassdeniss.brickview.data.model;

public class User {
    private String _id;
    private String username;
    private String email;
    private String image;
    private Set[] sets;
    private Tokens tokens;

    public String getId() {
        return this._id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setSets(Set[] sets) {
        this.sets = sets;
    }

    public void setTokens(Tokens tokens) {
        this.tokens = tokens;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

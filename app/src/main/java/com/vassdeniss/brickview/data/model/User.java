package com.vassdeniss.brickview.data.model;

import java.util.List;

public class User {
    private String _id;
    private String username;
    private String email;
    private String image;
    private List<Set> sets;
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

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getImage() {
        return this.image;
    }

    public Tokens getTokens() {
        return this.tokens;
    }

    public List<Set> getSets() {
        return this.sets;
    }

    public void setSets(final List<Set> sets) {
        this.sets = sets;
    }

    public void setTokens(final Tokens tokens) {
        this.tokens = tokens;
    }

    public void setImage(final String image) {
        this.image = image;
    }
}

package com.vassdeniss.brickview.data.model;

public class Set {
    private String _id;
    private String setNum;
    private String name;
    private int year;
    private int parts;
    private String image;
    private int minifigCount;
    private Minifigure[] minifigs;
    private String username;
    private String userImage;
    private String[] videoIds;
    private String review;
    private String reviewDate;

    public String getId() {
        return this._id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getName() {
        return this.name;
    }

    public String getSetId() {
        return this._id;
    }

    public String getImage() {
        return this.image;
    }

    public int getYear() {
        return this.year;
    }

    public int getParts() {
        return this.parts;
    }

    public String getUserImage() {
        return this.userImage;
    }

    public String getReview() {
        return this.review;
    }

    public String getDate() {
        return this.reviewDate;
    }
}

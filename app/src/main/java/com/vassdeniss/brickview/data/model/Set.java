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
    private String user;
    private String[] videoIds;

    public String getUsername() {
        return this.user;
    }

    public String getName() {
        return this.name;
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

    public String getU() {
        return this.name;
    }

//    public String getName() {
//        return this.name;
//    }
//
//            this.username = username;
//        this.name = name;
//        this.image = image;
//        this.userImage = userImage;
//        this.reviewDate = reviewData;
}

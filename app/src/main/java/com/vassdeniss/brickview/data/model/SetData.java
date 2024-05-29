package com.vassdeniss.brickview.data.model;

public class SetData {
    private String _id;
    private String username;
    private String reviewDate;
    private String userImage;
    private String name;
    private String image;

    public SetData(String username, String name, String image, String userImage, String reviewData) {
        this.username = username;
        this.name = name;
        this.image = image;
        this.userImage = userImage;
        this.reviewDate = reviewData;
    }

    public String getId() {
        return this._id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getDate() {
        return this.reviewDate;
    }

    public String getUserImage() {
        return this.userImage;
    }

    public String getName() {
        return this.name;
    }

    public String getImageUrl() {
        return this.image;
    }
}

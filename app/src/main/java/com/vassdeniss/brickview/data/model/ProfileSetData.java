package com.vassdeniss.brickview.data.model;

public class ProfileSetData {
    private final String _id;
    private String setNum;
    private final String year;
    private final String parts;
    private final String name;
    private final String image;

    public ProfileSetData(String setId, String year, String name, String image, String parts) {
        this._id = setId;
        this.year = year;
        this.name = name;
        this.image = image;
        this.parts = parts;
    }

    public String getSetId() {
        return this._id;
    }

    public String getYear() {
        return this.year;
    }

    public String getParts() {
        return this.parts;
    }

    public String getName() {
        return this.name;
    }

    public String getImageUrl() {
        return this.image;
    }
}

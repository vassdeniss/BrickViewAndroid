package com.vassdeniss.brickview.data.model;

public class ProfileSetData {
    private final String _id;
    private String setNum;
    private final String year;
    private final String parts;
    private final String name;
    private final String image;

    public ProfileSetData(Set set) {
        this._id = set.getSetId();
        this.year = String.valueOf(set.getYear());
        this.name = set.getName();
        this.image = set.getImage();
        this.parts = String.valueOf(set.getParts());
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

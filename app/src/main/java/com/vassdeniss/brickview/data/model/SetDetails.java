package com.vassdeniss.brickview.data.model;

import java.util.List;

public class SetDetails {
    private String _id;
    private String setName;
    private String setImage;
    private String setNumber;
    private int setParts;
    private int setYear;
    private int setMinifigCount;
    private List<String> setImages;
    private List<String> setVideoIds;
    private List<Minifigure> setMinifigures;
    private String userId;
    private String userUsername;
    private String content;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public String getSetImage() {
        return setImage;
    }

    public void setSetImage(String setImage) {
        this.setImage = setImage;
    }

    public String getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(String setNumber) {
        this.setNumber = setNumber;
    }

    public int getSetParts() {
        return setParts;
    }

    public void setSetParts(int setParts) {
        this.setParts = setParts;
    }

    public int getSetYear() {
        return setYear;
    }

    public void setSetYear(int setYear) {
        this.setYear = setYear;
    }

    public int getSetMinifigCount() {
        return setMinifigCount;
    }

    public void setSetMinifigCount(int setMinifigCount) {
        this.setMinifigCount = setMinifigCount;
    }

    public List<String> getSetImages() {
        return setImages;
    }

    public void setSetImages(List<String> setImages) {
        this.setImages = setImages;
    }

    public List<String> getSetVideoIds() {
        return setVideoIds;
    }

    public void setSetVideoIds(List<String> setVideoIds) {
        this.setVideoIds = setVideoIds;
    }

    public List<Minifigure> getSetMinifigures() {
        return setMinifigures;
    }

    public void setSetMinifigures(List<Minifigure> setMinifigures) {
        this.setMinifigures = setMinifigures;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static class Minifigure {
        private String name;
        private int count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}

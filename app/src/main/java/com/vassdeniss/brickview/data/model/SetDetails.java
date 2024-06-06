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

    public void setId(final String _id) {
        this._id = _id;
    }

    public String getSetName() {
        return setName;
    }

    public String getSetImage() {
        return setImage;
    }

    public String getSetNumber() {
        return setNumber;
    }

    public int getSetParts() {
        return setParts;
    }

    public int getSetYear() {
        return setYear;
    }

    public int getSetMinifigCount() {
        return setMinifigCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }
}

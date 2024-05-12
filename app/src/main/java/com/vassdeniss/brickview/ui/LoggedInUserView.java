package com.vassdeniss.brickview.ui;

public class LoggedInUserView {
    private final String displayName;

    public LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}

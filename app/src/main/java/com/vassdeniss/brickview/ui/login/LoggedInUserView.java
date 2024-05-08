package com.vassdeniss.brickview.ui.login;

class LoggedInUserView {
    private final String displayName;

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return this.displayName;
    }
}

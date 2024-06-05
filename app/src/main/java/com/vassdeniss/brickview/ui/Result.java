package com.vassdeniss.brickview.ui;

import androidx.annotation.Nullable;

public class Result {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private String error;

    public Result(@Nullable String error) {
        this.error = error;
    }

    public Result(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    public LoggedInUserView getSuccess() {
        return this.success;
    }

    @Nullable
    public String getError() {
        return this.error;
    }
}

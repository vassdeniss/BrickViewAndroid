package com.vassdeniss.brickview.ui.register;

import androidx.annotation.Nullable;

import com.vassdeniss.brickview.ui.LoggedInUserView;

class RegisterResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private String error;

    RegisterResult(@Nullable String error) {
        this.error = error;
    }

    RegisterResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return this.success;
    }

    @Nullable
    String getError() {
        return this.error;
    }
}

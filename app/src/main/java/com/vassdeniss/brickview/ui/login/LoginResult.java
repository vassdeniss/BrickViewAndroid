package com.vassdeniss.brickview.ui.login;

import androidx.annotation.Nullable;

class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private String error;

    LoginResult(@Nullable String error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success) {
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

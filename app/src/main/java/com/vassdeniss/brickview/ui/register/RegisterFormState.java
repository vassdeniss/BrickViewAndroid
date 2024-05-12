package com.vassdeniss.brickview.ui.register;

import androidx.annotation.Nullable;

public class RegisterFormState {
    @Nullable
    private final Integer usernameError;
    private final boolean isDataValid;

    RegisterFormState(@Nullable Integer usernameError) {
        this.usernameError = usernameError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.usernameError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return this.usernameError;
    }

    boolean isDataValid() {
        return this.isDataValid;
    }
}

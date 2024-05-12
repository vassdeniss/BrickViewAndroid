package com.vassdeniss.brickview.ui.register;

import androidx.annotation.Nullable;

public class RegisterFormState {
    @Nullable
    private final Integer usernameError;
    @Nullable
    private final Integer emailError;
    @Nullable
    private final Integer passwordError;
    @Nullable
    private final Integer repeatPasswordError;
    private final boolean isDataValid;

    RegisterFormState(
            @Nullable Integer usernameError,
            @Nullable Integer emailError,
            @Nullable Integer passwordError,
            @Nullable Integer repeatPasswordError) {
        this.usernameError = usernameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.repeatPasswordError = repeatPasswordError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.usernameError = null;
        this.emailError = null;
        this.passwordError = null;
        this.repeatPasswordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return this.usernameError;
    }

    @Nullable
    Integer getEmailError() {
        return this.emailError;
    }

    @Nullable
    Integer getPasswordError() {
        return this.passwordError;
    }

    @Nullable
    Integer getRepeatPasswordError() {
        return this.repeatPasswordError;
    }

    boolean isDataValid() {
        return this.isDataValid;
    }
}

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

    public RegisterFormState(
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

    public RegisterFormState(boolean isDataValid) {
        this.usernameError = null;
        this.emailError = null;
        this.passwordError = null;
        this.repeatPasswordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getUsernameError() {
        return this.usernameError;
    }

    @Nullable
    public Integer getEmailError() {
        return this.emailError;
    }

    @Nullable
    public Integer getPasswordError() {
        return this.passwordError;
    }

    @Nullable
    public Integer getRepeatPasswordError() {
        return this.repeatPasswordError;
    }

    boolean isDataValid() {
        return this.isDataValid;
    }
}

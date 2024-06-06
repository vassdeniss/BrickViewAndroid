package com.vassdeniss.brickview.ui;

import androidx.annotation.Nullable;

public class Result<T> {
    @Nullable
    private T success;
    @Nullable
    private String error;

    public Result(@Nullable String error) {
        this.error = error;
    }

    public Result(@Nullable T success) {
        this.success = success;
    }

    @Nullable
    public T getSuccess() {
        return this.success;
    }

    @Nullable
    public String getError() {
        return this.error;
    }
}

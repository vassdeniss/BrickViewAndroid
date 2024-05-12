package com.vassdeniss.brickview.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vassdeniss.brickview.R;

public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private final MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();

    LiveData<RegisterFormState> getRegisterFormState() {
        return this.registerFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return this.registerResult;
    }

    public void registerDataChanged(String username, String password) {
        if (!this.isUserNameValid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null));
        } else if (this.isUserNameShort(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.short_username, null));
        } else if (!this.isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password));
        } else if (this.isPasswordShort(password)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.short_password));
        }
    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }

        return !username.trim().isEmpty();
    }

    private boolean isUserNameShort(String username) {
        return username.length() < 4;
    }

    private boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }

        return !password.trim().isEmpty();
    }

    private boolean isPasswordShort(String password) {
        return password.length() < 8;
    }
}

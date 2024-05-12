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

    public void registerDataChanged(String username) {
        if (!this.isUserNameValid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username));
        }
    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }

        if (username.trim().isEmpty()) {
            return false;
        }

        return username.length() >= 4;
    }
}

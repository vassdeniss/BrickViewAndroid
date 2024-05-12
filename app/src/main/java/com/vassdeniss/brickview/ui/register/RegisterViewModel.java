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

    public void registerDataChanged(String username,
                                    String email,
                                    String password,
                                    String repeatPassword) {
        if (this.isFieldInvalid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null, null, null));
        } else if (this.isFieldShort(username, 4)) {
            registerFormState.setValue(new RegisterFormState(R.string.short_username, null, null, null));
        }

        if (this.isFieldInvalid(email) || this.isEmailInvalid(email)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_email, null, null));
        }

        if (this.isFieldInvalid(password)) {
            registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_password, null));
        } else if (this.isFieldShort(password, 8)) {
            registerFormState.setValue(new RegisterFormState(null, null, R.string.short_password, null));
        }

        if (this.isFieldInvalid(repeatPassword)) {
            registerFormState.setValue(new RegisterFormState(null, null, null, R.string.invalid_password));
        } else if (this.doPasswordsMismatch(password, repeatPassword)) {
            registerFormState.setValue(new RegisterFormState(null, null, null, R.string.password_mismatch));
        }
    }

    private boolean isFieldInvalid(String field) {
        if (field == null) {
            return true;
        }

        return field.trim().isEmpty();
    }

    private boolean isFieldShort(String field, int length) {
        return field.length() < length;
    }

    private boolean isEmailInvalid(String email) {
        return !email.contains("@");
    }

    private boolean doPasswordsMismatch(String password, String repeatPassword) {
        if (password == null) {
            return true;
        }

        return !password.equals(repeatPassword);
    }
}

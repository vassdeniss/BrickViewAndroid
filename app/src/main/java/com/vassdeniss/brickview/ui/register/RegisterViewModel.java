package com.vassdeniss.brickview.ui.register;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.vassdeniss.brickview.R;
import com.vassdeniss.brickview.VolleyRequestHelper;
import com.vassdeniss.brickview.data.UserRepository;
import com.vassdeniss.brickview.data.model.Response;
import com.vassdeniss.brickview.ui.LoggedInUserView;
import com.vassdeniss.brickview.ui.Result;

import org.json.JSONObject;

import java.util.Objects;

public class RegisterViewModel extends ViewModel {
    private final UserRepository userRepository = UserRepository.getInstance();
    private final MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private final MutableLiveData<Result<LoggedInUserView>> result = new MutableLiveData<>();

    public LiveData<RegisterFormState> getRegisterFormState() {
        return this.registerFormState;
    }

    public LiveData<Result<LoggedInUserView>> getResult() {
        return this.result;
    }

    public void register(final String username,
                         final String email,
                         final String password,
                         final String repeatPassword,
                         final Context context) {
        VolleyRequestHelper.VolleyCallback<JSONObject> callbacks = new VolleyRequestHelper.VolleyCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonResult) {
                Gson gson = new Gson();
                Response res = gson.fromJson(jsonResult.toString(), Response.class);
                res.user.setImage(res.image);
                res.user.setTokens(res.tokens);
                userRepository.setLoggedInUser(res.user);

                final String username = userRepository.getLoggedInUser().getUsername();
                result.setValue(
                        new Result<>(new LoggedInUserView(username))
                );
            }

            @Override
            public void onError(VolleyError error) {
                final String message = VolleyRequestHelper.defaultErrorCallback(error);
                result.setValue(new Result<>(message));
            }
        };

        new VolleyRequestHelper.Builder()
                .setContext(context)
                .useMethod(Request.Method.POST)
                .toUrl("/users/register")
                .withBody(VolleyRequestHelper.createBody("username", username, "email", email, "password", password, "repeatPassword", repeatPassword))
                .addCallback(callbacks)
                .execute();
    }

    public void registerDataChanged(String username,
                                    String email,
                                    String password,
                                    String repeatPassword) {
        if (this.isFieldInvalid(username)) {
            this.registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null, null, null));
        } else if (this.isFieldShort(username, 4)) {
            this.registerFormState.setValue(new RegisterFormState(R.string.short_username, null, null, null));
        } else if (this.isFieldInvalid(email) || this.isEmailInvalid(email)) {
            this.registerFormState.setValue(new RegisterFormState(null, R.string.invalid_email, null, null));
        } else if (this.isFieldInvalid(password)) {
            this.registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_password, null));
        } else if (this.isFieldShort(password, 8)) {
            this.registerFormState.setValue(new RegisterFormState(null, null, R.string.short_password, null));
        } else if (this.isFieldInvalid(repeatPassword)) {
            this.registerFormState.setValue(new RegisterFormState(null, null, null, R.string.invalid_password));
        } else if (this.doPasswordsMismatch(password, repeatPassword)) {
            this.registerFormState.setValue(new RegisterFormState(null, null, null, R.string.password_mismatch));
        } else {
            this.registerFormState.setValue(new RegisterFormState(true));
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

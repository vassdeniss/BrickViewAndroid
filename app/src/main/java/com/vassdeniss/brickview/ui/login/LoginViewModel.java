package com.vassdeniss.brickview.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;

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

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<Result<LoggedInUserView>> result = new MutableLiveData<>();
    private final UserRepository userRepository = UserRepository.getInstance();

    public LiveData<LoginFormState> getLoginFormState() {
        return this.loginFormState;
    }

    public LiveData<Result<LoggedInUserView>> getResult() {
        return this.result;
    }

    public void login(final String username,
                      final String password,
                      final Context context) {
        final VolleyRequestHelper.VolleyCallback<JSONObject> callbacks = new VolleyRequestHelper.VolleyCallback<JSONObject>() {
            @Override
            public void onSuccess(final JSONObject jsonResult) {
                final Gson gson = new Gson();
                final Response res = gson.fromJson(jsonResult.toString(), Response.class);
                res.user.setImage(res.image);
                res.user.setTokens(res.tokens);
                userRepository.setLoggedInUser(res.user);

                final String username = userRepository.getLoggedInUser().getUsername();
                result.setValue(
                        new Result<>(new LoggedInUserView(username))
                );
            }

            @Override
            public void onError(final VolleyError error) {
                final String message = VolleyRequestHelper.defaultErrorCallback(error);
                if (!Objects.equals(message, "")) {
                    result.setValue(new Result(message));
                }
            }
        };

        new VolleyRequestHelper.Builder()
                .setContext(context)
                .useMethod(Request.Method.POST)
                .toUrl("/users/login")
                .withBody(VolleyRequestHelper.createBody("username", username, "password", password))
                .addCallback(callbacks)
                .execute();
    }

    public void loginDataChanged(final String username, final String password) {
        if (!this.isUserNameValid(username)) {
            this.loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!this.isPasswordValid(password)) {
            this.loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            this.loginFormState.setValue(new LoginFormState(true));
        }
    }

    private boolean isUserNameValid(final String username) {
        if (username == null) {
            return false;
        }

        return !username.trim().isEmpty();
    }

    private boolean isPasswordValid(final String password) {
        return password != null;
    }
}

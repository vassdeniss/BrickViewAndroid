package com.vassdeniss.brickview.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.vassdeniss.brickview.R;
import com.vassdeniss.brickview.VolleyRequestHelper;
import com.vassdeniss.brickview.data.UserRepository;
import com.vassdeniss.brickview.data.model.Response;
import com.vassdeniss.brickview.ui.LoggedInUserView;

import org.json.JSONObject;

import java.util.Objects;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final UserRepository userRepository;

    LoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return this.loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return this.loginResult;
    }

    public void login(String username, String password, Context context) {
        VolleyRequestHelper helper = VolleyRequestHelper.getInstance(context);

        VolleyRequestHelper.VolleyCallback<JSONObject> callbacks = new VolleyRequestHelper.VolleyCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                Gson gson = new Gson();
                Response res = gson.fromJson(result.toString(), Response.class);
                res.user.setImage(res.image);
                res.user.setTokens(res.tokens);
                userRepository.setLoggedInUser(res.user);
                loginResult.setValue(
                        new LoginResult(
                                new LoggedInUserView(
                                        userRepository.getLoggedInUser().getUsername()
                                )
                        )
                );
            }

            @Override
            public void onError(VolleyError error) {
                String message = helper.defaultErrorCallback(error);
                if (!Objects.equals(message, "")) {
                    loginResult.setValue(new LoginResult(message));
                }
            }
        };

        new VolleyRequestHelper.Builder()
                .setContext(context)
                .useMethod(Request.Method.POST)
                .toUrl("/users/login")
                .withBody(helper.createBody("username", username, "password", password))
                .addCallback(callbacks)
                .execute();
    }

    public void loginDataChanged(String username, String password) {
        if (!this.isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!this.isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }

        return !username.trim().isEmpty();
    }

    private boolean isPasswordValid(String password) {
        return password != null;
    }
}

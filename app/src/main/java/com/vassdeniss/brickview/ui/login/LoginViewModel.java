package com.vassdeniss.brickview.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vassdeniss.brickview.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    LiveData<LoginFormState> getLoginFormState() {
        return this.loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return this.loginResult;
    }

    public void login(String username, String password, Context context) {
        String url = "https://brickview.api.vasspass.net/users/login";

        JSONObject body = new JSONObject();

        try {
            body.put("username", username);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                response -> {
                    Log.d("Response", response.toString());
                    // Handle successful login response
                    // this.loginRepository.setLoggedInUser();
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorMessage = new String(error.networkResponse.data);
                            JSONObject jsonObject = new JSONObject(errorMessage);
                            String message = jsonObject.getString("message");
                            this.loginResult.setValue(new LoginResult(message));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


        Volley.newRequestQueue(context).add(request);
//        if (result instanceof Result.Success) {
//            User data = ((Result.Success<User>) result).getData();
//            this.loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
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

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
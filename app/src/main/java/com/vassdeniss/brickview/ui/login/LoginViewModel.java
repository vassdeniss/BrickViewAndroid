package com.vassdeniss.brickview.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.util.Patterns;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vassdeniss.brickview.R;
import com.vassdeniss.brickview.data.UserRepository;
import com.vassdeniss.brickview.data.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private UserRepository userRepository;

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
        String url = "https://brickview.api.vasspass.net";

        JSONObject body = new JSONObject();

        try {
            body.put("username", username);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url + "/users/login", body,
                response -> {
                    Gson gson = new Gson();
                    Response res = gson.fromJson(response.toString(), Response.class);
                    res.user.setImage(res.image);
                    this.userRepository.setLoggedInUser(res.user);
                    this.loginResult.setValue(new LoginResult(new LoggedInUserView(res.user.getUsername())));
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

        StringRequest healthRequest = new StringRequest(Request.Method.GET, url + "/health",
                response -> queue.add(request), null);


        queue.add(healthRequest);
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
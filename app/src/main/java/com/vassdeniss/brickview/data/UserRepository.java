package com.vassdeniss.brickview.data;

import com.google.gson.Gson;
import com.vassdeniss.brickview.data.model.Response;
import com.vassdeniss.brickview.data.model.User;

import org.json.JSONObject;

public class UserRepository {
    private static volatile UserRepository instance;

    private User user = null;

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }

        return instance;
    }

    public boolean isLoggedIn() {
        return this.user != null;
    }

    public void logout() {
        this.user = null;
    }

    public User getLoggedInUser() {
        return this.user;
    }

    public void setLoggedInUser(User user) {
        this.user = user;
    }

    public void updateUser(JSONObject result) {
        Gson gson = new Gson();
        Response res = gson.fromJson(result.toString(), Response.class);
        res.user.setImage(res.image);
        res.user.setTokens(res.tokens);
        this.setLoggedInUser(res.user);
    }
}

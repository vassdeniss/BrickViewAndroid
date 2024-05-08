package com.vassdeniss.brickview.data;

import com.vassdeniss.brickview.data.model.User;

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
}

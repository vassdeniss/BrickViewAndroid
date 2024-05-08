package com.vassdeniss.brickview;

import android.view.MenuItem;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vassdeniss.brickview.data.UserRepository;

public class BottomNavigationHelper {
    public static void updateNav(FragmentActivity activity) {
        BottomNavigationView navView = activity.findViewById(R.id.nav_view);
        MenuItem profile = navView.getMenu().findItem(R.id.navigation_profile);
        MenuItem login = navView.getMenu().findItem(R.id.navigation_login);
        boolean isLogged = UserRepository.getInstance().isLoggedIn();
        if (isLogged) {
            profile.setVisible(true);
            login.setVisible(false);
        } else {
            profile.setVisible(false);
            login.setVisible(true);
        }
    }
}

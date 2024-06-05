package com.vassdeniss.brickview;

import android.view.MenuItem;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vassdeniss.brickview.data.UserRepository;

public class BottomNavigationHelper {
    public static void updateNav(final FragmentActivity activity) {
        final BottomNavigationView navView = activity.findViewById(R.id.nav_view);
        final MenuItem profile = navView.getMenu().findItem(R.id.navigation_profile);
        final MenuItem login = navView.getMenu().findItem(R.id.navigation_login);
        final MenuItem register = navView.getMenu().findItem(R.id.navigation_register);

        final boolean isLogged = UserRepository.getInstance().isLoggedIn();
        if (isLogged) {
            profile.setVisible(true);
            login.setVisible(false);
            register.setVisible(false);
        } else {
            profile.setVisible(false);
            login.setVisible(true);
            register.setVisible(true);
        }
    }
}

package deskind.com.rollingwheels;

import android.support.v4.app.Fragment;

public class Fragmentator {
    private static Fragment currentFragment;

    public static void setCurrentFragment(Fragment fragment) {
        currentFragment = fragment;
    }

    public static Fragment getCurrentFragment() {
        return currentFragment;
    }
}

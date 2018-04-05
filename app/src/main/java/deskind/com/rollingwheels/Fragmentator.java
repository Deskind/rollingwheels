package deskind.com.rollingwheels;

import android.support.v4.app.Fragment;

public class Fragmentator {
    private static Fragment currentFragment = null;

    public static void setCurrentFragment(Fragment currentFragment) {
        currentFragment = currentFragment;
    }

    public static Fragment getCurrentFragment() {
        return currentFragment;
    }
}

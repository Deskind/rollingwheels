package deskind.com.rollingwheels.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import deskind.com.rollingwheels.activities.MnActivity;

public class CarsPagerAdapter extends FragmentPagerAdapter {

    public CarsPagerAdapter(FragmentManager manager){
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return MnActivity.sliderFragments.get(position);
    }

    @Override
    public int getCount() {
        return MnActivity.sliderFragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}

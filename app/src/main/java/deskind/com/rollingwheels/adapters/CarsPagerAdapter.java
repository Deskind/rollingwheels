package deskind.com.rollingwheels.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import deskind.com.rollingwheels.activities.MnActivity;
import deskind.com.rollingwheels.fragments.CarFragment;

public class CarsPagerAdapter extends FragmentPagerAdapter {

    private List<CarFragment> fragments;


    public CarsPagerAdapter(FragmentManager manager, List<CarFragment> fragments){
        super(manager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    public void setFragments(List<CarFragment> fragments) {
        this.fragments = fragments;
    }
}

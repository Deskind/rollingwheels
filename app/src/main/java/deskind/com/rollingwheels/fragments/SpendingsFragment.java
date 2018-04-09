package deskind.com.rollingwheels.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import deskind.com.rollingwheels.Fragmentator;
import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.SpendingsCalculator;
import deskind.com.rollingwheels.activities.MnActivity;

public class SpendingsFragment extends Fragment {
    SpendingsCalculator calculator = new SpendingsCalculator();
    ViewPager pager;

    private TextView tvFuel, tvRepairs,tvFluids, tvFilters, fuelSpendings, repairsSpendings, fluidsSpendings, filtersSpendings;
    private ImageView ivFuelList, ivRepairsList, ivFluidsList, ivFiltersList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.spendings_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pager = MnActivity.pager;

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                Log.i("CLASS", Fragmentator.getCurrentFragment().getClass().toString());
                if(Fragmentator.getCurrentFragment() instanceof SpendingsFragment) {
                    setFuelUpSpendings();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        fuelSpendings = getView().findViewById(R.id.fuel_spendings);
        setFuelUpSpendings();
    }

    public void setFuelUpSpendings (){
        fuelSpendings.setText(String.format("%.1f", calculator.calcFuelSpendings(getActivity(), pager.getCurrentItem())));
    }
}
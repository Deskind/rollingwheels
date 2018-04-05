package deskind.com.rollingwheels.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.SpendingsCalculator;

public class SpendingsFragment extends Fragment {

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

        SpendingsCalculator calculator = new SpendingsCalculator();
        ViewPager pager = getActivity().findViewById(R.id.pager);
        int pagerPosition = pager.getCurrentItem();

        fuelSpendings = getView().findViewById(R.id.fuel_spendings);
//        fuelSpendings.setText(String.format("%.1f", calculator.calcFuelSpendings(getActivity(), pagerPosition)));
    }
}
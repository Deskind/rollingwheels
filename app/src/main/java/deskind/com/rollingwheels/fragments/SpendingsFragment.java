package deskind.com.rollingwheels.fragments;

import android.content.Context;
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
    public static SpendingsCalculator calculator = new SpendingsCalculator();
    public static TextView fuelSpendings, repairsSpendings, fluidsSpendings, filtersSpendings;
    public static ImageView ivFuelList, ivRepairsList, ivFluidsList, ivFiltersList;

    private static Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.spendings_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        fuelSpendings = getView().findViewById(R.id.fuel_spendings);
        repairsSpendings = getView().findViewById(R.id.repairs_spendings);
        fluidsSpendings = getView().findViewById(R.id.fluids_spendings);
        filtersSpendings = getView().findViewById(R.id.filters_spendings);

    }

    public void setSpendings (int currentItem){
        fuelSpendings.setText(String.format("%.1f", calculator.calcFuelSpendings(context, currentItem)));
        repairsSpendings.setText(String.valueOf(calculator.calcRepairSpendings(context, currentItem)));
        fluidsSpendings.setText(String.valueOf(calculator.calcFluidsSpendings(context, currentItem)));
        filtersSpendings.setText(String.valueOf(calculator.calcFiltersSpendings(context, currentItem)));
    }

}
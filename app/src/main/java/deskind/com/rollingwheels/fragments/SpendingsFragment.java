package deskind.com.rollingwheels.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.activities.MainActivity;
import deskind.com.rollingwheels.activities.MnActivity;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.FuelUp;

public class SpendingsFragment extends Fragment {

    private static TextView tvFuel, tvRepairs,tvFluids, tvFilters, fuelSpendings, repairsSpendings, fluidsSpendings, filtersSpendings;
    private static ImageView ivFuelList, ivRepairsList, ivFluidsList, ivFiltersList;

    private static Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.spendings_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fuelSpendings = getView().findViewById(R.id.fuel_spendings);
        context = getActivity();

        fuelSpendings.setText(String.format("%.1f", calcFuelSpendings(MnActivity.getCurrentCar())));
    }

    public static float calcFuelSpendings(String carBrand){
        float fuelSpendings = 0;

        List<FuelUp> fuelUps = DBUtility.getAppDatabase(context).getCarsDao().getFuelUps();
        for(FuelUp fuelUp : fuelUps){
            if(fuelUp.getCarBrand().equals(MnActivity.getCurrentCar())){
                fuelSpendings+=fuelUp.getCost()*fuelUp.getLiters();
            }
        }
        return fuelSpendings;
    }
}

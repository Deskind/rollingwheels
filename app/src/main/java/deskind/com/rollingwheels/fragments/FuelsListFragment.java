package deskind.com.rollingwheels.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.activities.MnActivity;
import deskind.com.rollingwheels.adapters.ExpandableFuelsListAdapter;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.FuelUp;

public class FuelsListFragment extends Fragment {

    public static ExpandableListView elv;

    public static ExpandableFuelsListAdapter adapter;
    public static List<FuelUp> headersList;
    public static HashMap<Integer, List<Integer>> mapContent;
    public static MnActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fuels_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //init
        activity = (MnActivity)getActivity();
        headersList = new ArrayList<>();
        mapContent = new HashMap<>();

        //views
        elv = getActivity().findViewById(R.id.elv_fuels);

        //car in the pager
        String car = getArguments().getString("name");

        //Get fuel ups from database
        if(activity.periodMode == false) {
            headersList = DBUtility.getAppDatabase(getContext()).getCarsDao().getFuelUpsFor(car);
        }else{
            headersList = DBUtility.getAppDatabase(getContext()).getCarsDao().getAllFuelUpsForBrandForPeriod(activity.fromDate, activity.toDate, car);
        }

        //Fill data
        for(FuelUp fuelUp : headersList){
            int id = fuelUp.getFuelUpId();
            int liters = fuelUp.getLiters();

            ArrayList<Integer> list = new ArrayList<>();
            list.add(liters);

            mapContent.put(id, list);
        }

        adapter = new ExpandableFuelsListAdapter(getActivity(), headersList, mapContent);

        elv.setAdapter(adapter);
    }

    public ExpandableFuelsListAdapter getAdapter() {
        return adapter;
    }

    public static void update(String carName) {

        headersList = new ArrayList<>();
        mapContent = new HashMap<>();

        //get data from db
        headersList = DBUtility.getAppDatabase(activity).getCarsDao().getFuelUpsFor(carName);

        //fill with data
        for(FuelUp fuelUp : headersList){
            int id = fuelUp.getFuelUpId();
            int liters = fuelUp.getLiters();

            ArrayList<Integer> list = new ArrayList<>();
            list.add(liters);

            mapContent.put(id, list);
        }

        //create adapter and set to extendable list view
        adapter = new ExpandableFuelsListAdapter(activity, headersList, mapContent);
        elv.setAdapter(adapter);
    }
}

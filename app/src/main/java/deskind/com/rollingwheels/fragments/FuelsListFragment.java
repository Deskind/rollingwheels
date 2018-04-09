package deskind.com.rollingwheels.fragments;

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
import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.adapters.ExpandableFuelsListAdapter;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.FuelUp;

public class FuelsListFragment extends Fragment {

    public static ExpandableFuelsListAdapter adapter;
    public static List<FuelUp> headersList;
    public static HashMap<Integer, List<Integer>> mapContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fuels_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //views
        ExpandableListView elv = getActivity().findViewById(R.id.elv_fuels);

        //get fuel ups from database
        List<FuelUp> fuelUps = DBUtility.getAppDatabase(getContext()).getCarsDao().getFuelUps();

        //initialize
        headersList = new ArrayList<>();
        mapContent = new HashMap<>();

        //car in the pager
        String name = getArguments().getString("name");

        //fiil with data
        for(FuelUp fuelUp : fuelUps){
            if(fuelUp.getCarBrand().equals(name)){
                headersList.add(fuelUp);
            }
        }

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
}

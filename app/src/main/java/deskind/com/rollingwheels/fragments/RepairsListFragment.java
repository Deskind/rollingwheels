package deskind.com.rollingwheels.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import deskind.com.rollingwheels.adapters.ExpandableRepairsListAdapter;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.Repair;

public class RepairsListFragment extends Fragment {

    private ExpandableListView elvRepairsList;
    private MnActivity activity;
    private ExpandableRepairsListAdapter adapter;
    private List<Repair> headers;
    private Map<Integer, List<String>> content;
    String carName;

    public ExpandableRepairsListAdapter getAdapter() {
        return adapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.repairs_list, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        elvRepairsList = view.findViewById(R.id.elv_repairs_list);
        activity = (MnActivity)getActivity();

        prepareData();
    }

    public void prepareData(){
        ViewPager pager = ((MnActivity)getActivity()).getPager();
        carName = MnActivity.cars.get(pager.getCurrentItem()).getCarBrand();

        if(activity.periodMode == false) {
            headers = DBUtility.getAppDatabase(activity).getCarsDao().getAllRapairsForBrand(carName);
        }else{
            headers = DBUtility.getAppDatabase(activity).getCarsDao().getAllRapairsForBrandForPeriod(activity.fromDate, activity.toDate, carName);
        }

        content = new HashMap<>();

        for(Repair r : headers){
            ArrayList<String> list = new ArrayList<>();
            list.add(r.getManufacturer());
            content.put(r.getRepairId(), list);
        }

        adapter = new ExpandableRepairsListAdapter(activity, headers, content, this);
        elvRepairsList.setAdapter(adapter);
    }

    public String getCarName() {
        return carName;
    }
}

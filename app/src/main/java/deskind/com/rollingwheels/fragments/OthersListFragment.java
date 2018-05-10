package deskind.com.rollingwheels.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import deskind.com.rollingwheels.adapters.ExpandableOthersListAdapter;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.OtherService;

public class OthersListFragment extends Fragment {
    private ExpandableListView elvFiltersList;
    private MnActivity activity;
    private ExpandableOthersListAdapter adapter;
    private List<OtherService> headers;
    private Map<Integer, List<String>> content;
    String carName;

    public ExpandableOthersListAdapter getAdapter() {
        return adapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.others_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        elvFiltersList = view.findViewById(R.id.elv_others_list);
        activity = (MnActivity)getActivity();
        prepareData();
    }

    public void prepareData(){
        ViewPager pager = ((MnActivity)getActivity()).getPager();
        carName = MnActivity.cars.get(pager.getCurrentItem()).getCarBrand();

        if(activity.periodMode == false) {
            headers = DBUtility.getAppDatabase(activity).getCarsDao().getAllOtherServices(carName);
        }else{
            headers = DBUtility.getAppDatabase(activity).getCarsDao().getAllOtherServicesForBrandForPeriod(activity.fromDate, activity.toDate, carName);
        }
        content = new HashMap<>();

        for(OtherService s : headers){
            ArrayList<String> list = new ArrayList<>();
            list.add(s.getOtherBrand());
            content.put(s.getServiceId(), list);
        }

        adapter = new ExpandableOthersListAdapter(activity, headers, content, this);
        elvFiltersList.setAdapter(adapter);
    }

    public String getCarName() {
        return carName;
    }
}

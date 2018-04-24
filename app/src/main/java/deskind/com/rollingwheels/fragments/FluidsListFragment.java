package deskind.com.rollingwheels.fragments;

import android.content.Context;
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
import deskind.com.rollingwheels.adapters.ExpandableFluidsListAdapter;
import deskind.com.rollingwheels.adapters.ExpandableRepairsListAdapter;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.FluidService;
import deskind.com.rollingwheels.entities.Repair;

public class FluidsListFragment extends Fragment{
    private ExpandableListView elvRepairsList;
    private Context context;
    private ExpandableFluidsListAdapter adapter;
    private List<FluidService> headers;
    private Map<Integer, List<String>> content;
    String carName;

    public ExpandableFluidsListAdapter getAdapter() {
        return adapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fluids_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        elvRepairsList = view.findViewById(R.id.elv_fluids_list);
        context = getActivity();
        prepareData();
    }

    public void prepareData(){
        ViewPager pager = ((MnActivity)getActivity()).getPager();
        carName = MnActivity.cars.get(pager.getCurrentItem()).getCarBrand();
        headers = DBUtility.getAppDatabase(context).getCarsDao().getAllFluidServices(carName);
        content = new HashMap<>();

        for(FluidService s : headers){
            ArrayList<String> list = new ArrayList<>();
            list.add(s.getFluidBrand());
            content.put(s.getServiceId(), list);
        }

        adapter = new ExpandableFluidsListAdapter(context, headers, content, this);
        elvRepairsList.setAdapter(adapter);
    }

    public String getCarName() {
        return carName;
    }
}
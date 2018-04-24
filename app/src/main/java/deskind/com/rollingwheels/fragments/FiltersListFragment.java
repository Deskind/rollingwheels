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
import deskind.com.rollingwheels.adapters.ExpandableFiltersListAdapter;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.FilterService;

public class FiltersListFragment extends Fragment{
    private ExpandableListView elvFiltersList;
    private Context context;
    private ExpandableFiltersListAdapter adapter;
    private List<FilterService> headers;
    private Map<Integer, List<String>> content;
    String carName;

    public ExpandableFiltersListAdapter getAdapter() {
        return adapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filters_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        elvFiltersList = view.findViewById(R.id.elv_filters_list);
        context = getActivity();
        prepareData();
    }

    public void prepareData(){
        ViewPager pager = ((MnActivity)getActivity()).getPager();
        carName = MnActivity.cars.get(pager.getCurrentItem()).getCarBrand();
        headers = DBUtility.getAppDatabase(context).getCarsDao().getAllFilterServices(carName);
        content = new HashMap<>();

        for(FilterService s : headers){
            ArrayList<String> list = new ArrayList<>();
            list.add(s.getFilterBrand());
            content.put(s.getServiceId(), list);
        }

        adapter = new ExpandableFiltersListAdapter(context, headers, content, this);
        elvFiltersList.setAdapter(adapter);
    }

    public String getCarName() {
        return carName;
    }
}


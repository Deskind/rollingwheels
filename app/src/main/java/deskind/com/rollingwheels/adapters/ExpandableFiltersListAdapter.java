package deskind.com.rollingwheels.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.Map;
import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.FilterService;
import deskind.com.rollingwheels.entities.FluidService;
import deskind.com.rollingwheels.entities.Repair;
import deskind.com.rollingwheels.fragments.FiltersListFragment;

public class ExpandableFiltersListAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<FilterService> headers;
    private Map<Integer, List<String>> content;
    private FiltersListFragment filtersListFragment;

    public ExpandableFiltersListAdapter(Context context, List<FilterService> headers, Map<Integer, List<String>> content, FiltersListFragment filtersListFragment) {
        this.context = context;
        this.headers = headers;
        this.content = content;
        this.filtersListFragment = filtersListFragment;
    }

    @Override
    public int getGroupCount() {
        return headers.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return content.get(headers.get(i).getServiceId()).size();
    }

    @Override
    public Object getGroup(int i) {
        return headers.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return content.get(headers.get(i).getServiceId()).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        if(view==null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.repairs_list_header, null);
        }

        //prepare data for the view
        TextView tvDate = view.findViewById(R.id.repairs_list_header_date);
        tvDate.setText(headers.get(i).getDate());
        TextView tvMileage = view.findViewById(R.id.repairs_list_header_mileage);
        tvMileage.setText(String.valueOf(headers.get(i).getMileage()));
        TextView tvPrice = view.findViewById(R.id.repairs_list_header_price);
        tvPrice.setText(String.valueOf(headers.get(i).getPrice()));
        TextView tvDescription = view.findViewById(R.id.repairs_list_header_description);
        tvDescription.setText(headers.get(i).getDescription());

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        if(view==null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.repairs_list_item, null);
        }

        TextView tvManufacturer = view.findViewById(R.id.repairs_list_item_manufacturer);
        tvManufacturer.setText(headers.get(i).getFilterBrand());

        //trying to set mileage after service
        TextView tvMilesAfter = view.findViewById(R.id.repairs_list_item_miles_after);
        long serviceMileage = headers.get(i).getMileage();

        long lastUserMileage = filtersListFragment.getActivity().getSharedPreferences("miles", Context.MODE_PRIVATE).getLong(filtersListFragment.getCarName(), 0);
        if(lastUserMileage != 0 ){
            long mileageAfterService = lastUserMileage - serviceMileage;
            tvMilesAfter.setText(String.valueOf(mileageAfterService));
        }


        final Button delBtn = view.findViewById(R.id.repairs_list_item_del);
        delBtn.setTag(headers.get(i));
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterService service = (FilterService)delBtn.getTag();
                DBUtility.getAppDatabase(context).getCarsDao().deleteFilterService(service.getServiceId());
                content.remove(service.getServiceId());
                headers.remove(service);
                filtersListFragment.getAdapter().notifyDataSetChanged();
                Toast.makeText(context, "Deleted ... ", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

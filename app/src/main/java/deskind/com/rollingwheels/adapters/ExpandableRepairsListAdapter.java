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
import deskind.com.rollingwheels.entities.Repair;
import deskind.com.rollingwheels.fragments.RepairsListFragment;

public class ExpandableRepairsListAdapter extends BaseExpandableListAdapter{

    private Context context;
    private List<Repair> headers;
    private Map<Integer, List<String>> content;
    private RepairsListFragment repairsListFragment;

    public ExpandableRepairsListAdapter(Context context, List<Repair> headers, Map<Integer, List<String>> content, RepairsListFragment repairsListFragment) {
        this.context = context;
        this.headers = headers;
        this.content = content;
        this.repairsListFragment = repairsListFragment;
    }

    @Override
    public int getGroupCount() {
        return headers.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return content.get(headers.get(i).getRepairId()).size();
    }

    @Override
    public Object getGroup(int i) {
        return headers.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return content.get(headers.get(i).getRepairId()).get(i1);
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
        tvPrice.setText(String.valueOf(headers.get(i).getPartPrice()));
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
        tvManufacturer.setText(headers.get(i).getManufacturer());
        final Button delBtn = view.findViewById(R.id.repairs_list_item_del);
        delBtn.setTag(headers.get(i));
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Repair r = (Repair)delBtn.getTag();
                DBUtility.getAppDatabase(context).getCarsDao().deleteRepair(r.getRepairId());
                content.remove(r.getRepairId());
                headers.remove(r);
                repairsListFragment.getAdapter().notifyDataSetChanged();
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

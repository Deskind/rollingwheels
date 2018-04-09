package deskind.com.rollingwheels.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.List;
import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.FuelUp;
import deskind.com.rollingwheels.fragments.FuelsListFragment;

public class ExpandableFuelsListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<FuelUp> headersList;
    private HashMap<Integer, List<Integer>> mapContent;

    public ExpandableFuelsListAdapter(Context context, List<FuelUp> headersList, HashMap<Integer, List<Integer>> mapContent) {
        this.context = context;
        this.headersList = headersList;
        this.mapContent = mapContent;
    }

    @Override
    public int getGroupCount() {
        return headersList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mapContent.get(headersList.get(i).getFuelUpId()).size();
    }

    @Override
    public Object getGroup(int i) {
        return headersList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mapContent.get(headersList.get(i).getFuelUpId()).get(i1);
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
        String date = headersList.get(i).getDate();
        float price = headersList.get(i).getCost();
        int liters = headersList.get(i).getLiters();

        if(view==null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fuels_list_header, null);
        }

        TextView tvDate = (TextView)view.findViewById(R.id.fuels_list_header_date);
        TextView tvPrice = (TextView)view.findViewById(R.id.fuels_list_header_price);

        tvDate.setText(date);
        tvPrice.setText(String.format("%.2f", price*liters));

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        int liters = headersList.get(i).getLiters();

        if(view==null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fuels_list_item, null);
        }

        TextView tvLiters = view.findViewById(R.id.fules_list_item_liters);
        final Button btnDel = view.findViewById(R.id.fuels_list_item_del);

        tvLiters.setText(String.valueOf(liters)+ " liters");
        btnDel.setTag(headersList.get(i));

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Deleted ... ", Toast.LENGTH_SHORT).show();

                //get fuel up by tag from the button and delete from DB
                FuelUp fuelUp = (FuelUp)btnDel.getTag();
                DBUtility.getAppDatabase(context).getCarsDao().deleteFuelUp(fuelUp.getFuelUpId());

                FuelsListFragment.headersList.remove(fuelUp);
                FuelsListFragment.mapContent.remove(fuelUp.getFuelUpId());

                FuelsListFragment.adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}

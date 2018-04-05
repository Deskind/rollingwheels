package deskind.com.rollingwheels.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.activities.MnActivity;

//Fragments
public class CarFragment extends Fragment {

    private int index;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        index = getArguments().getInt("SLIDER_INDEX");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)getLayoutInflater().inflate(R.layout.car_fragment, container, false);
        TextView carName = (TextView)rootView.findViewById(R.id.tv_car_name);


        String name = getArguments().getString("CAR_NAME");
        carName.setText(name);

        return rootView;
    }

}

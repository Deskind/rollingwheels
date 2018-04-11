package deskind.com.rollingwheels.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.activities.MnActivity;

//Fragments
public class CarFragment extends Fragment {

    private int index;
    private TextView carName;
    private TextView carMileage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        index = getArguments().getInt("SLIDER_INDEX");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)getLayoutInflater().inflate(R.layout.car_fragment, container, false);

        carName = rootView.findViewById(R.id.tv_car_name);
        carMileage = rootView.findViewById(R.id.tv_car_mileage);

        String name = getArguments().getString("CAR_NAME");
        carName.setText(name);

        if(index == 0){
            setMileage(getArguments().getLong("mileage"));
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    public void setMileage(long userText) {
        carMileage.setText(String.valueOf(userText));
    }
}

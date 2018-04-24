package deskind.com.rollingwheels.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.activities.MnActivity;

public class CarFragment extends Fragment {

    private int index;
    private TextView carName;
    private TextView carMileage;
    private static boolean initialFragment = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)getLayoutInflater().inflate(R.layout.car_fragment, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carName = view.findViewById(R.id.tv_car_name);
        carMileage = view.findViewById(R.id.tv_car_mileage);

        String name = getArguments().getString("NAME");
        index = getArguments().getInt("INDEX");

//        if(index == 0 && initialFragment == true){
//            SharedPreferences miles = getActivity().getSharedPreferences("miles", Context.MODE_PRIVATE);
//            carName.setText(name);
//            setMileage(miles.getLong(name, 0));
//            initialFragment = false;
//            return;
//        }
        SharedPreferences miles = getActivity().getSharedPreferences("miles", Context.MODE_PRIVATE);
        carName.setText(name);
        setMileage(miles.getLong(name, 0));

        carName.setText(name);
    }

    public void setMileage(long userText) {
        carMileage.setText(String.valueOf(userText));
    }

}

package deskind.com.rollingwheels.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.activities.MnActivity;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.Car;

public class DeleteCarFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_car, container, false);

        final EditText etCar = view.findViewById(R.id.tv_car_to_delete);
        Button deleteBtn = view.findViewById(R.id.btn_del_car);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean isCarExists;
                int argumentsIndex = -1;
                String userText = etCar.getText().toString();

                //if user text is empty
                if(userText.isEmpty()) {
                    Toast.makeText(getActivity(), "Name can't be empty ...", Toast.LENGTH_SHORT).show();
                    return;
                }else{//if user text is not empty check name for existence in the database
                    //if the variable evaluates to false that means that car with such name not exists in a database
                    isCarExists = DBUtility.checkCar(userText);

                    if(!isCarExists){
                        Toast.makeText(getActivity(), "Sory, but no such car ...", Toast.LENGTH_LONG).show();
                        etCar.setText("");
                        return;
                    }
                }

                //delete car from database
                DBUtility.getAppDatabase(getActivity()).getCarsDao().deleteCar(userText);

                //Message
                Toast.makeText(getActivity(), "Car deleted ...", Toast.LENGTH_SHORT).show();

                //update data in collection by clearing and adding fragments with new indexes
//                MnActivity.sliderFragments.clear();
                List<Car> cars = DBUtility.getAppDatabase(getActivity()).getCarsDao().getAllCars();
                if(!cars.isEmpty()) {
                    for (int i = 0; i < cars.size(); i++) {
                        CarFragment f = new CarFragment();
                        Bundle b = new Bundle();
                        b.putInt("SLIDER_INDEX", i);
                        b.putString("CAR_NAME", cars.get(i).getCarBrand());
                        f.setArguments(b);
//                        MnActivity.sliderFragments.add(f);
                    }
                }

                //restart main activity
                getActivity().finish();
                startActivity(new Intent(getActivity(), MnActivity.class));

            }
        });

        return view;
    }
}

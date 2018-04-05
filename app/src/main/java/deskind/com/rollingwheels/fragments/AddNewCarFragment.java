package deskind.com.rollingwheels.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.activities.MnActivity;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.Car;

public class AddNewCarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_car, container, false);

        final EditText etName = v.findViewById(R.id.tv_new_car_name);
        Button doneBtn = v.findViewById(R.id.btn_new_car_done);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isCarExists;
                String userText = etName.getText().toString();

                //if user text is empty
                if(userText.isEmpty()) {
                    Toast.makeText(getActivity(), "Name can't be empty ...", Toast.LENGTH_SHORT).show();
                    return;
                }else{//if user text is not empty check name for existence in the database
                    //if the variable evaluates to true that means that car with such name already exists in a database
                    isCarExists = DBUtility.checkCar(userText);

                    if(isCarExists){
                        Toast.makeText(getActivity(), "Sory, but such name already exists ...", Toast.LENGTH_LONG).show();
                        etName.setText("");
                        return;
                    }
                }

                //add new fragment to cars pager
                CarFragment f = new CarFragment();
                Bundle b = new Bundle();
                b.putInt("SLIDER_INDEX", MnActivity.getSliderFragments().size());
                b.putString("CAR_NAME", userText);
                f.setArguments(b);

                //add fragment to collection
                MnActivity.sliderFragments.add(f);

                //notify adapter
                MnActivity.adapter.notifyDataSetChanged();


                //add new car to database
                DBUtility.getAppDatabase(getActivity()).getCarsDao().insertNewCar(new Car(userText));

                Toast.makeText(getActivity(), "New car added ...", Toast.LENGTH_SHORT).show();

                //return to spendings fragment
                getFragmentManager().popBackStackImmediate();

                //move pager to newly created car
                MnActivity.pager.setCurrentItem(MnActivity.sliderFragments.size()-1);
            }
        });

        return v;
    }
}

package deskind.com.rollingwheels.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import deskind.com.rollingwheels.Fragmentator;
import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.activities.MnActivity;
import deskind.com.rollingwheels.database.AppDatabase;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.Car;
import deskind.com.rollingwheels.entities.FuelUp;

public class FuelUpFragment extends Fragment {
    private final String FUEL_PRICE = "FUEL_PRICE";

    private static TextView fuelUpDate;
    private static EditText fuelPrice;
    private static EditText fuelUpValue;
    private static Button fuelUpDoneBtn;

    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fuel_up_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //trying to get fuel price from shared preferences
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        fuelUpDate = getView().findViewById(R.id.fuel_up_date);
        fuelPrice = getView().findViewById(R.id.fuel_price);
        fuelUpDoneBtn = getView().findViewById(R.id.fuel_up_done);
        fuelUpValue = getView().findViewById(R.id.fuel_up_value);

        //set up fuel price edit text
        fuelPrice.setText(sharedPref.getString(FUEL_PRICE, ""));

        //prepare date field
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String date = year +"-"+(month+1)+"-"+day;
        fuelUpDate.setText(date);



        //text view Date listener
        fuelUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateClicked(view);
            }
        });

        fuelUpDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fuelUpDoneClicked(view);
            }
        });
    }

    public void dateClicked(View v){
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getActivity().getSupportFragmentManager(), "Date picker fragment");
    }

    public void fuelUpDoneClicked(View v) {
        //add fuel price to shared preferences if it was changed or if it not exists yet
        if(!sharedPref.contains(FUEL_PRICE)){
            editor.putString(FUEL_PRICE, fuelPrice.getText().toString());
            editor.commit();
        }else{
            String savedPrice = sharedPref.getString(FUEL_PRICE, null);
            if(!savedPrice.equals(fuelPrice.getText().toString())){
                editor.putString(FUEL_PRICE, fuelPrice.getText().toString());
                editor.commit();
            }
        }

        if (fuelPrice.getText().toString().equals("") ||
            fuelUpValue.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "You need to fill all filds", Toast.LENGTH_SHORT).show();
        } else {
            AppDatabase appDatabase = DBUtility.getAppDatabase(getActivity());
            ViewPager pager = getActivity().findViewById(R.id.pager);
            List<Car> cars = appDatabase.getCarsDao().getAllCars();
            String carName = cars.get(pager.getCurrentItem()).getCarBrand();

            DecimalFormat formatter = new DecimalFormat("#0.00");

            String fuelUpDate = this.fuelUpDate.getText().toString();
            float fuelUpPrice = Float.valueOf(this.fuelPrice.getText().toString());
            int fuelUpValue = Integer.valueOf(this.fuelUpValue.getText().toString());

            FuelUp fuelUp = new FuelUp(carName, fuelUpDate, fuelUpValue, fuelUpPrice);
            appDatabase.getCarsDao().insertFuelUp(fuelUp);

            Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();

            getActivity().getSupportFragmentManager().popBackStackImmediate();
            Fragmentator.setCurrentFragment(null);
        }

    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String date = year +"-"+(month+1)+"-"+day;
            fuelUpDate.setText(date);
        }
    }
}

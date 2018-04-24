package deskind.com.rollingwheels.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.activities.MnActivity;
import deskind.com.rollingwheels.dao.CarsDAO;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.FilterService;
import deskind.com.rollingwheels.entities.FluidService;
import deskind.com.rollingwheels.entities.Repair;

public class ServiceFragment extends Fragment {

    public static TextView serviceDate;
    public EditText mileage, partManufacturer, description, price;
    public Button serviceDone;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         return inflater.inflate(R.layout.service_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init
        context = getActivity();

        //find views
        serviceDate = getView().findViewById(R.id.service_date);
        mileage = getView().findViewById(R.id.service_mileage);
        partManufacturer = getView().findViewById(R.id.part_manufacturer);
        description = getView().findViewById(R.id.service_description);
        price = getView().findViewById(R.id.service_price);
        serviceDone = getView().findViewById(R.id.service_done);

        //listeners
        serviceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        serviceDone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ViewPager pager = ((MnActivity)getActivity()).getPager();
                final String sDate = serviceDate.getText().toString();
                final String sMileage = mileage.getText().toString();
                final String sManufacturer = partManufacturer.getText().toString();
                final String sDescription = description.getText().toString();
                final String sPrice = price.getText().toString();

                CarsDAO carsDao = DBUtility.getAppDatabase(context).getCarsDao();
                String serviceType = getArguments().getString("type");
                String carName = MnActivity.cars.get(pager.getCurrentItem()).getCarBrand();

                if(sMileage.equals("")|
                   sManufacturer.equals("")|
                        sDescription.equals("")|
                            sPrice.equals("")){
                    Toast.makeText(context, "Fill empty fields ...", Toast.LENGTH_SHORT).show();
                }else{
                    if(serviceType.equals("repair")){
                        carsDao.insertRepair(new Repair(carName,
                                sDate,
                                Long.valueOf(sMileage),
                                sManufacturer,
                                sDescription,
                                Integer.valueOf(sPrice)));
                    }else if(serviceType.equals("fluid")){
                        carsDao.insertFluidService(new FluidService(carName,
                                sDate,
                                Long.valueOf(sMileage),
                                sManufacturer,
                                Integer.valueOf(sPrice),
                                sDescription));
                    }else if(serviceType.equals("filter")){
                        carsDao.insertFilterService(new FilterService(carName,
                                sDate,
                                Long.valueOf(sMileage),
                                sManufacturer,
                                Integer.valueOf(sPrice),
                                sDescription));
                    }
                    Toast.makeText(context, "Done ...", Toast.LENGTH_SHORT).show();
                    ((MnActivity)getActivity()).getSpendingsFragment().setSpendings(pager.getCurrentItem());
                    closeFragment();
                }


            }
        });

        //prepare date field
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String date = year +"-"+(month+1)+"-"+day;
        serviceDate.setText(date);
    }

    public void closeFragment(){
        getFragmentManager().popBackStackImmediate();
    }

    private void showDateDialog() {
        SelectDateFragment selectDateFragment = new SelectDateFragment();
        selectDateFragment.show(getActivity().getSupportFragmentManager(), "Select Date Fragment");
    }


    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

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

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            String date = year +"-"+(month+1)+"-"+day;
            serviceDate.setText(date);
        }
    }
}

package deskind.com.rollingwheels.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;

import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.activities.MnActivity;

public class SpendingsPeriodFragment extends Fragment {

    private static EditText dateFrom;
    private static EditText dateTo;
    private Button doneBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.set_period, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        dateFrom = view.findViewById(R.id.period_from);
        dateTo = view.findViewById(R.id.period_to);
        doneBtn = view.findViewById(R.id.btn_period_done);

        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectDateFragment selectDateFragment = new SelectDateFragment();
                Bundle b = new Bundle();
                b.putString("fromto", "from");
                selectDateFragment.setArguments(b);
                selectDateFragment.show(getFragmentManager(), null);
            }
        });

        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectDateFragment selectDateFragment = new SelectDateFragment();
                Bundle b = new Bundle();
                b.putString("fromto", "to");
                selectDateFragment.setArguments(b);
                selectDateFragment.show(getFragmentManager(), null);
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting text values from views
                String from = dateFrom.getText().toString();
                String to = dateTo.getText().toString();

                //check on empty user input
                if(from.equals("") | to.equals("")){
                    Toast.makeText(getActivity(), "Fill all fields ...", Toast.LENGTH_SHORT).show();
                    return;
                //if user's text is not empty
                }else{
                    MnActivity activity = (MnActivity)getActivity();
                    int currentItem = activity.getPager().getCurrentItem();
                    SpendingsFragment spendingsFragment = activity.getSpendingsFragment();

                    //close fragment
                    getFragmentManager().popBackStackImmediate();

                    //setting spendings for period
                    spendingsFragment.setSpendingsForPeriod(currentItem,dateFrom.getText().toString(), dateTo.getText().toString());
                    //set value of text view on main screen
                    activity.tv_period.setText("from " + from + "  to " + to);
                    //turn app into period mode
                    activity.setPeriodMode(true);
                    activity.fromDate = from;
                    activity.toDate = to;
                }

            }
        });
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
            month++;

            DecimalFormat formatter = new DecimalFormat("00");

            String m = formatter.format(Double.valueOf(month));
            String d = formatter.format(Double.valueOf(day));

            String date = year +"-"+m+"-"+d;

            String fromto = getArguments().getString("fromto");
            switch (fromto){
                case "from" :
                    dateFrom.setText(date);
                    break;
                case "to" :
                    dateTo.setText(date);
                    break;
            }
        }
    }
}

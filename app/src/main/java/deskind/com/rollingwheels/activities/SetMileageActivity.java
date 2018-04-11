package deskind.com.rollingwheels.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.fragments.CarFragment;

public class SetMileageActivity extends Activity{

    private EditText mileage;
    private Button done;
    private Activity activity = this;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_mileage);

        sharedPreferences = this.getSharedPreferences("miles",Context.MODE_PRIVATE);

        //find views
        mileage = findViewById(R.id.et_new_mileage);
        done = findViewById(R.id.btn_mileage_done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pagerName = MnActivity.cars.get(MnActivity.pager.getCurrentItem()).getCarBrand();
                long userText = Long.valueOf(mileage.getText().toString());

                //write value to shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong(pagerName, userText);
                editor.commit();

                CarFragment f = MnActivity.sliderFragments.get(MnActivity.pager.getCurrentItem());

                f.setMileage(userText);

                activity.finish();
            }
        });
    }
}

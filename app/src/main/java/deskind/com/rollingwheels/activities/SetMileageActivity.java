package deskind.com.rollingwheels.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.entities.Car;
import deskind.com.rollingwheels.fragments.CarFragment;

public class SetMileageActivity extends Activity{

    private EditText mileage;
    private Button done;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_mileage);

        //find views
        mileage = findViewById(R.id.et_new_mileage);
        done = findViewById(R.id.btn_mileage_done);

        done.setOnClickListener(new MileageDoneClicked());
    }

    class MileageDoneClicked implements View.OnClickListener{
    @Override
        public void onClick(View view) {
            String userText = mileage.getText().toString();
            if(!userText.equals("")){
                //get name from intent
                String carName = getIntent().getExtras().getString("CarName");
                SharedPreferences miles = getSharedPreferences("miles", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = miles.edit().putLong(carName, Long.valueOf(userText));
                editor.commit();
                //intent for sending result back to main activity
                Intent intent = new Intent();
                intent.putExtra("mileage", userText);
                setResult(RESULT_OK, intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Field cant be empty", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

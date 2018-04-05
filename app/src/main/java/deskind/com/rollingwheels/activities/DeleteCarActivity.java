package deskind.com.rollingwheels.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.Car;

public class DeleteCarActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_car);

        Button delCarBtn = findViewById(R.id.btn_del_car);
        final EditText etCarToDelete = findViewById(R.id.tv_car_to_delete);

        delCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isCarThere = false;
                String name = etCarToDelete.getText().toString();
                List<Car> cars;
                if(name.equals("")){
                    Toast.makeText(getApplicationContext(), "Name can't be empty", Toast.LENGTH_SHORT).show();
                }else{
                    cars = DBUtility.getAppDatabase(getApplicationContext()).getCarsDao().getAllCars();
                    if(!cars.isEmpty()) {
                        for (Car c : cars) {
                            if (c.getCarBrand().equals(name)) {
                                DBUtility.getAppDatabase(getApplicationContext()).getCarsDao().deleteCar(name);
                                isCarThere = true;
                                Toast.makeText(getApplicationContext(), "Deleted...", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                }

                if(!isCarThere){
                    Toast.makeText(getApplicationContext(), "Car not found...", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}

package deskind.com.rollingwheels;

import android.content.Context;

import java.util.List;

import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.Car;
import deskind.com.rollingwheels.entities.FuelUp;

public class SpendingsCalculator {

    public float calcFuelSpendings(Context context, int pagerPosition){
        float fuelSpendings = 0;

        List<FuelUp> fuelUps = DBUtility.getAppDatabase(context).getCarsDao().getFuelUps();
        List<Car> cars = DBUtility.getAppDatabase(context).getCarsDao().getAllCars();

        String carName = cars.get(pagerPosition).getCarBrand();

        for(FuelUp fuelUp : fuelUps){
            if(fuelUp.getCarBrand().equals(carName)){
                fuelSpendings+=fuelUp.getCost()*fuelUp.getLiters();
            }
        }
        return fuelSpendings;
    }
}

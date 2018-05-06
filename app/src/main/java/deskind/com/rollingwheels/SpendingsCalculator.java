package deskind.com.rollingwheels;

import android.content.Context;

import java.util.List;

import deskind.com.rollingwheels.activities.MnActivity;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.Car;
import deskind.com.rollingwheels.entities.FilterService;
import deskind.com.rollingwheels.entities.FluidService;
import deskind.com.rollingwheels.entities.FuelUp;
import deskind.com.rollingwheels.entities.Repair;

public class SpendingsCalculator {

    public float calcFuelSpendings(Context context, int pagerPosition){
        float fuelSpendings = 0;

        List<FuelUp> fuelUps = DBUtility.getAppDatabase(context).getCarsDao().getFuelUpsFor(MnActivity.cars.get(pagerPosition).getCarBrand());

        if(!fuelUps.isEmpty()) {
            for (FuelUp fuelUp : fuelUps) {
                fuelSpendings += fuelUp.getCost() * fuelUp.getLiters();
            }
        }

        return fuelSpendings;
    }

    public int calcRepairSpendings(Context context, int currentItem) {
        int spendings = 0;

        List<Repair> repairs = DBUtility.getAppDatabase(context).getCarsDao().getAllRapairsForBrand(MnActivity.cars.get(currentItem).getCarBrand());
        if(!repairs.isEmpty()){
            for(Repair r : repairs){
                spendings+=r.getPartPrice();
            }
            return spendings;
        }
        return spendings;
    }


    public int calcFluidsSpendings(Context context, int currentItem) {
        int spendings = 0;

        List<FluidService> services = DBUtility.getAppDatabase(context).getCarsDao().getAllFluidServices(MnActivity.cars.get(currentItem).getCarBrand());
        if(!services.isEmpty()){
            for(FluidService service : services){
                spendings+=service.getPrice();
            }
        }
        return spendings;
    }

    public int calcFiltersSpendings(Context context, int currentItem) {
        int spendings = 0;

        List<FilterService> services = DBUtility.getAppDatabase(context).getCarsDao().getAllFilterServices(MnActivity.cars.get(currentItem).getCarBrand());
        if(!services.isEmpty()){
            for(FilterService service : services){
                spendings+=service.getPrice();
            }
        }
        return spendings;
    }

    public int calcRepairSpendingsForPeriod(Context context, int currentItem, String fromString, String toString) {
        int spendings = 0;

        List<Repair> repairs = DBUtility.getAppDatabase(context).getCarsDao().getAllRapairsForBrandForPeriod(MnActivity.cars.get(currentItem).getCarBrand(), fromString, toString);
        if(!repairs.isEmpty()){
            for(Repair r : repairs){
                spendings+=r.getPartPrice();
            }
            return spendings;
        }

        return spendings;
    }
}

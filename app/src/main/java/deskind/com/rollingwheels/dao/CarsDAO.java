package deskind.com.rollingwheels.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

import deskind.com.rollingwheels.entities.Car;
import deskind.com.rollingwheels.entities.FilterService;
import deskind.com.rollingwheels.entities.FluidService;
import deskind.com.rollingwheels.entities.FuelUp;
import deskind.com.rollingwheels.entities.Repair;

@Dao
public interface CarsDAO {
    @Insert
    public void insertNewCar(Car car);

    @Insert
    public void insertFuelUp(FuelUp fuelUp);

    @Insert
    public void insertRepair(Repair repair);

    @Insert
    public void insertFluidService(FluidService fluidService);

    @Insert
    public void insertFilterService(FilterService fluidService);

    @Query("SELECT * FROM 'Car' where car_brand = :car_brand")
    public Car getCar(String car_brand);

    @Query("SELECT * FROM FuelUp")
    public List<FuelUp> getFuelUps();

    @Query("SELECT * FROM FuelUp where car_brand = :car_brand")
    public List<FuelUp> getFuelUpsFor(String car_brand);

    @Query("SELECT * FROM 'Car'")
    public List<Car> getAllCars();

    @Query("DELETE FROM FuelUp where fuelup_id = :id")
    public void deleteFuelUp(int id);

    @Query("SELECT car_brand FROM car")
    public String[] getAllCarBrands();

    @Query("SELECT * FROM FluidService where car_brand = :carBrand ORDER BY \"Date\" DESC")
    public List<FluidService> getAllFluidServices(String carBrand);

    @Query("SELECT * FROM FilterService where car_brand = :carBrand ORDER BY \"mileage\" DESC ")
    public List<FilterService> getAllFilterServices(String carBrand);

    @Query("SELECT * FROM Repair where CarBrand = :carBrand ORDER BY \"Пробег\" DESC")
    public List<Repair> getAllRapairsForBrand(String carBrand);

    @Query("select * from Repair where \"Дата\" between \"2017-12-01\" and \"2018-05-01\"")
    public List<Repair> getAllRapairsForBrandForPeriod(String carBrand, String fromString, String toString);

    @Query("SELECT price FROM FluidService where car_brand = :carBrand")
    public int[] getFluidServicesTotalCost(String carBrand);

    @Query("SELECT price FROM FilterService where car_brand = :carBrand")
    public int[] getFilterServicesTotalCost(String carBrand);

    @Query("DELETE from Car where car_brand = :carBrand")
    public void deleteCar(String carBrand);

    @Query("DELETE from Repair where ID = :repairId")
    public void deleteRepair(int repairId);

    @Query("DELETE from FluidService where service_id = :serviceId")
    public void deleteFluidService(int serviceId);

    @Query("DELETE from FilterService where service_id = :serviceId")
    public void deleteFilterService(int serviceId);

}

package deskind.com.rollingwheels.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import deskind.com.rollingwheels.dao.CarsDAO;
import deskind.com.rollingwheels.entities.Car;
import deskind.com.rollingwheels.entities.FilterService;
import deskind.com.rollingwheels.entities.FluidService;
import deskind.com.rollingwheels.entities.FuelUp;
import deskind.com.rollingwheels.entities.OtherService;
import deskind.com.rollingwheels.entities.Repair;


@Database(version = 3, entities = {Car.class, FuelUp.class, Repair.class, FluidService.class, FilterService.class, OtherService.class})
public abstract class AppDatabase extends RoomDatabase {
abstract public CarsDAO getCarsDao();
}

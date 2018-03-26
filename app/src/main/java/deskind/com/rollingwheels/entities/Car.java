package deskind.com.rollingwheels.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = @Index(value = {"car_brand"}, unique = true))
public class Car {
    @PrimaryKey(autoGenerate = true)
    private int car_id;
    @ColumnInfo(name = "car_brand")
    private String carBrand;


    public Car() {
    }

    public Car(String carBrand) {
        this.carBrand = carBrand;
    }

    public int getCar_id() {
        return car_id;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }
}

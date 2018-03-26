package deskind.com.rollingwheels.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity (foreignKeys = @ForeignKey(entity = Car.class,
                                    parentColumns = "car_brand",
                                    childColumns = "car_brand",
                                    onDelete = ForeignKey.CASCADE))
public class FuelUp {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "fuelup_id")
    private int fuelUpId;

    @ColumnInfo(name = "car_brand")
    private String carBrand;

    //YYYY-MM-DD HH:MM:SS.SSS
    private String date;

    private int liters;

    private float cost;

    public FuelUp(String carBrand, String date, int liters, float cost) {
        this.carBrand = carBrand;
        this.date = date;
        this.liters = liters;
        this.cost = cost;
    }

    public void setFuelUpId(int fuelUpId) {
        this.fuelUpId = fuelUpId;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLiters(int liters) {
        this.liters = liters;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public int getFuelUpId() {
        return fuelUpId;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public String getDate() {
        return date;
    }

    public int getLiters() {
        return liters;
    }

    public float getCost() {
        return cost;
    }
}

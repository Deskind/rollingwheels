package deskind.com.rollingwheels.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity (foreignKeys = @ForeignKey(entity = Car.class,
        parentColumns = "car_brand",
        childColumns = "car_brand",
        onDelete = ForeignKey.CASCADE))
public class FilterService {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "service_id")
    public int serviceId;

    @ColumnInfo(name = "car_brand")
    public String carBrand;

    @ColumnInfo(name = "Date")
    public String date;

    @ColumnInfo(name = "mileage")
    public long mileage;

    @ColumnInfo(name = "filter_brand")
    public String filterBrand;

    @ColumnInfo(name = "price")
    public int price;

    @ColumnInfo(name = "description")
    public String description;


    public FilterService(String carBrand,String date, long mileage, String filterBrand, int price, String description) {
        this.carBrand = carBrand;
        this.date = date;
        this.mileage = mileage;
        this.filterBrand = filterBrand;
        this.price = price;
        this.description = description;
    }

    public FilterService(){}

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMileage(long mileage) {
        this.mileage = mileage;
    }

    public void setFilterBrand(String filterBrand) {
        this.filterBrand = filterBrand;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public String getDate() {
        return date;
    }

    public long getMileage() {
        return mileage;
    }

    public String getFilterBrand() {
        return filterBrand;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}

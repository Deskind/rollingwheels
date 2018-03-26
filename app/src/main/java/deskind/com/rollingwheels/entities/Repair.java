package deskind.com.rollingwheels.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity (foreignKeys = @ForeignKey(entity = Car.class,
        parentColumns = "car_brand",
        childColumns = "CarBrand",
        onDelete = ForeignKey.CASCADE))
public class Repair {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public int repairId;

    @ColumnInfo(name = "CarBrand")
    public String carBrand;

    //YYYY-MM-DD
    @ColumnInfo(name = "Дата")
    public String date;

    @ColumnInfo(name = "Пробег")
    public long mileage;

    @ColumnInfo(name = "Производитель запчасти")
    public String manufacturer;

    @ColumnInfo(name = "Арт/Номер")
    public String partNumber;

    @ColumnInfo(name = "Описание")
    public String description;

    @ColumnInfo(name = "Цена")
    public int partPrice;

    public Repair(String carBrand, String date, long mileage, String manufacturer, String partNumber, String description, int partPrice) {
        this.carBrand = carBrand;
        this.date = date;
        this.mileage = mileage;
        this.manufacturer = manufacturer;
        this.partNumber = partNumber;
        this.description = description;
        this.partPrice = partPrice;
    }

    public int getRepairId() {
        return repairId;
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

    public String getManufacturer() {
        return manufacturer;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public String getDescription() {
        return description;
    }

    public int getPartPrice() {
        return partPrice;
    }
}

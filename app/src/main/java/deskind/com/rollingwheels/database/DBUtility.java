package deskind.com.rollingwheels.database;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;


public class DBUtility {
    //DB reference
    private static AppDatabase appDatabase = null;

    //Migration implementation
    static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("create table FluidService("+
            "service_id INTEGER PRIMARY KEY NOT NULL,"+
            "car_brand TEXT,"+
            "mileage INTEGER NOT NULL,"+
            "fluid_brand TEXT,"+
            "price INTEGER NOT NULL,"+
            "description TEXT,"+
            "FOREIGN KEY (car_brand) REFERENCES Car(car_brand) ON DELETE NO ACTION"+
            ");");
        }
    };

    static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE FluidService "
                    + "ADD COLUMN Date TEXT");
        }
    };


    public static AppDatabase getAppDatabase(Context context){
        if(appDatabase == null){
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, "cars").
                    allowMainThreadQueries().
                    fallbackToDestructiveMigration().
                    build();
            return  appDatabase;
        }else {
            return appDatabase;
        }
    }

}

package deskind.com.rollingwheels.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import deskind.com.rollingwheels.Fragmentator;
import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.SpendingsCalculator;
import deskind.com.rollingwheels.adapters.CarsPagerAdapter;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.Car;
import deskind.com.rollingwheels.fragments.AddNewCarFragment;
import deskind.com.rollingwheels.fragments.CarFragment;
import deskind.com.rollingwheels.fragments.DeleteCarFragment;
import deskind.com.rollingwheels.fragments.FuelUpFragment;
import deskind.com.rollingwheels.fragments.FuelsListFragment;
import deskind.com.rollingwheels.fragments.RepairsListFragment;
import deskind.com.rollingwheels.fragments.RepairsListFragment;
import deskind.com.rollingwheels.fragments.ServiceFragment;
import deskind.com.rollingwheels.fragments.SpendingsFragment;


public class MnActivity extends AppCompatActivity {

    private FloatingActionButton fabPlus, fabFuel, fabRepair, fabFluid, fabFilter;
    private  Animation open, close, clockwise, anticlockwise;

    private static boolean isFabOpen = false;

    public static ViewPager pager;
    public static CarsPagerAdapter adapter;

    public static List<Car> cars;
    public static List<CarFragment> sliderFragments;

    private static SpendingsCalculator calculator;
    
    private FragmentManager fragmentManager;

    private SpendingsFragment spendingsFragment;
    private RepairsListFragment repairsListFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_const);

        //find views
        pager = findViewById(R.id.pager);
        ImageButton addNewCar = findViewById(R.id.ib_add_car);

        //objects
        SharedPreferences sharedPreferences = this.getSharedPreferences("miles", Context.MODE_PRIVATE);
        calculator = new SpendingsCalculator();
        spendingsFragment = new SpendingsFragment();
        fragmentManager = getSupportFragmentManager();

        //fab buttons
        fabPlus = findViewById(R.id.fab_plus);
        fabFuel = findViewById(R.id.fab_fuel);
        fabRepair = findViewById(R.id.fab_repair);
        fabFluid = findViewById(R.id.fab_fluid);
        fabFilter = findViewById(R.id.fab_filter);

        //load animations
        open = getAnimation(R.anim.fab_open, this);
        close = getAnimation(R.anim.fab_close, this);
        clockwise = getAnimation(R.anim.rotate_clockwise, this);
        anticlockwise = getAnimation(R.anim.rotate_anticlockwise, this);

        //listeners
        fabPlus.setOnClickListener(new FabPlusClicked());

        fabFuel.setOnClickListener(new OnFabClicked());

            //set tag for fabs
            fabFuel.setTag(new FuelUpFragment());

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                //trying to set mileage for current car
                long mileage = getSharedPreferences("miles", Context.MODE_PRIVATE).getLong(MnActivity.cars.get(MnActivity.pager.getCurrentItem()).getCarBrand(), 0);
                if(mileage != 0) {
                    sliderFragments.get(position).setMileage(mileage);
                }

                //update spendings
                SpendingsFragment.setFuelUpSpendings();
                SpendingsFragment.setRepairSpendings();

                //update fuels list fragment (car name as method argument)
                if(FuelsListFragment.elv != null){
                    FuelsListFragment.update(cars.get(pager.getCurrentItem()).getCarBrand());
                }

                if(repairsListFragment != null){
                    repairsListFragment.prepareData();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        //initialize collection for fragments that will be in a pager
        sliderFragments = new ArrayList<>();

        //get cars from database
        cars = DBUtility.getAppDatabase(this).getCarsDao().getAllCars();

        //create adapter
        adapter = new CarsPagerAdapter(fragmentManager);

        //Create fragment for every car in a list and add it to sliderFragments
        if(!cars.isEmpty()) {
            for (int i = 0; i < cars.size(); i++) {
                CarFragment f = new CarFragment();
                Bundle b = new Bundle();
                b.putInt("SLIDER_INDEX", i);
                b.putString("CAR_NAME", cars.get(i).getCarBrand());
                if(i == 0){
                    long l = getSharedPreferences("miles", Context.MODE_PRIVATE).getLong(MnActivity.cars.get(MnActivity.pager.getCurrentItem()).getCarBrand(), 0);
                    b.putLong("mileage", l);
                }
                f.setArguments(b);
                sliderFragments.add(f);
            }
        }

        //set adapter to pager
        pager.setAdapter(adapter);

        //show spendings fragment in central fragment
        showFragment(spendingsFragment, R.id.central_fragment);

        //if no available cars in app
        if(cars.isEmpty()){
            Toast.makeText(this, "Add a car ...", Toast.LENGTH_LONG).show();
            replaceFragment(R.id.central_fragment, new AddNewCarFragment());
        }

    }

    public void addNewCar(View v){
        replaceFragment(R.id.central_fragment, new AddNewCarFragment());
    }

    public void addNewService(View v){
        ServiceFragment f = new ServiceFragment();

        Bundle b = new Bundle();
        b.putString("type", "repair");
        f.setArguments(b);

        replaceFragment(R.id.central_fragment, f);

        closeFabs();
    }
    
    public void deleteCar(View v){
        replaceFragment(R.id.central_fragment, new DeleteCarFragment());
    }

    public void showFuelsList(View v){
        FuelsListFragment fuelsListFragment = new FuelsListFragment();

        Bundle b = new Bundle();
        b.putString("name", cars.get(pager.getCurrentItem()).getCarBrand());
        fuelsListFragment.setArguments(b);

        replaceFragment(R.id.central_fragment, fuelsListFragment);
    }

    public void showRepairsList(View v){
        repairsListFragment = new RepairsListFragment();
        replaceFragment(R.id.central_fragment, repairsListFragment);
    }

    public void showSetMileageActivity(View v){
        Intent intent = new Intent(this, SetMileageActivity.class);
        startActivity(intent);
    }

    public void setCurrentFragment(Fragment fragment){
        Fragmentator.setCurrentFragment(fragment);
    }

    private void showFragment(Fragment f, int containerId){
        setCurrentFragment(f);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(containerId, f).commit();
    }

    public void replaceFragment(int containerId, Fragment f){
        Fragmentator.setCurrentFragment(f);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(containerId, f);
        ft.addToBackStack(null);
        ft.commit();
    }

    private Animation getAnimation(int animId, Context context){
        return AnimationUtils.loadAnimation(context, animId);
    }

    private void closeFabs(){
        fabPlus.startAnimation(anticlockwise);
        fabFuel.startAnimation(close);
        fabFuel.setClickable(false);
        fabRepair.startAnimation(close);
        fabRepair.setClickable(false);
        fabFluid.startAnimation(close);
        fabFluid.setClickable(false);
        fabFilter.startAnimation(close);
        fabFilter.setClickable(false);

        isFabOpen = false;
    }

    private void openFabs(){
        fabPlus.startAnimation(clockwise);
        fabFuel.startAnimation(open);
        fabFuel.setClickable(true);
        fabRepair.startAnimation(open);
        fabRepair.setClickable(true);
        fabFluid.startAnimation(open);
        fabFluid.setClickable(true);
        fabFilter.startAnimation(open);
        fabFilter.setClickable(true);

        isFabOpen = true;
    }

    //INNER CLASSES
    //Fabs click listeners
    class OnFabClicked implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            replaceFragment(R.id.central_fragment, (Fragment) view.getTag());
            closeFabs();
        }
    }

    //inner classes
    class FabPlusClicked implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(isFabOpen){
                closeFabs();
            }else{
                openFabs();
            }
        }
    }
}
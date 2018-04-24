package deskind.com.rollingwheels.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import deskind.com.rollingwheels.Fragmentator;
import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.adapters.CarsPagerAdapter;
import deskind.com.rollingwheels.dao.CarsDAO;
import deskind.com.rollingwheels.database.AppDatabase;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.Car;
import deskind.com.rollingwheels.entities.FilterService;
import deskind.com.rollingwheels.entities.FluidService;
import deskind.com.rollingwheels.entities.Repair;
import deskind.com.rollingwheels.fragments.AddNewCarFragment;
import deskind.com.rollingwheels.fragments.CarFragment;
import deskind.com.rollingwheels.fragments.DeleteCarFragment;
import deskind.com.rollingwheels.fragments.FiltersListFragment;
import deskind.com.rollingwheels.fragments.FluidsListFragment;
import deskind.com.rollingwheels.fragments.FuelUpFragment;
import deskind.com.rollingwheels.fragments.FuelsListFragment;
import deskind.com.rollingwheels.fragments.RepairsListFragment;
import deskind.com.rollingwheels.fragments.ServiceFragment;
import deskind.com.rollingwheels.fragments.SpendingsFragment;


public class MnActivity extends FragmentActivity {

    private FloatingActionButton fabPlus, fabFuel, fabRepair, fabFluid, fabFilter;
    private  Animation open, close, clockwise, anticlockwise;

    //fabs open or close flag
    private static boolean isFabOpen = false;

    private ViewPager p;
    //temp pager reference
    private ViewPager pager;

    private CarsPagerAdapter carsAdapter;

    public static List<Car> cars;

    private FragmentManager fragmentManager;
    private SpendingsFragment spendingsFragment;

    private List<CarFragment> carFragments;

    private RepairsListFragment repairsListFragment;
    private FluidsListFragment fluidsListFragment;
    private FiltersListFragment filtersListFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_const);

        Log.i("Create","Called");
        //init
        AppDatabase database = DBUtility.getAppDatabase(this);

        p = findViewById(R.id.pager);
        //init temp
        pager = p;

        fragmentManager = getSupportFragmentManager();

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

        fragmentManager.addOnBackStackChangedListener(new MyBackStackChangedListener());
        fabFuel.setOnClickListener(new FabFuelClicked());
        fabRepair.setOnClickListener(new FabRepairClicked());

        //get all cars from db
        cars = database.getCarsDao().getAllCars();

        //initialize list of fragments
        carFragments = new ArrayList<>();

        //creating fragments
        createCarsFragments(cars, carFragments);

        //create adapter for slider
        carsAdapter = new CarsPagerAdapter(getSupportFragmentManager(), carFragments);

        //set adapter to pager
        p.setAdapter(carsAdapter);

        pager.setOnPageChangeListener(new CarsPagerListener());

        //hide sp_fragment from main layout
        if(fragmentManager.getBackStackEntryCount() != 0){
            findViewById(R.id.sp_fragment).setVisibility(View.INVISIBLE);
        }

        //close fabs
        closeFabs();

        //calcutate spendings for first element in slider if such exists
        if(!cars.isEmpty()) {
            spendingsFragment = (SpendingsFragment)fragmentManager.findFragmentById(R.id.sp_fragment);
            spendingsFragment.setSpendings(0);
        }else{
            replaceFragment(R.id.central_fragment, new AddNewCarFragment());
            Toast.makeText(this, "Add a car ...", Toast.LENGTH_LONG).show();
        }



    }



    public List<CarFragment> getSliderFragments() {
        return carFragments;
    }

    public PagerAdapter getPagerAdapter(){
        return carsAdapter;
    }

    public ViewPager getPager(){
        return pager;
    }

    public SpendingsFragment getSpendingsFragment(){
        return spendingsFragment;
    }

    //methods fills list of fragments based on cars list
    private void createCarsFragments(List<Car> cars, List<CarFragment> carFragments) {
        CarFragment f;
        Bundle b;

        for(int i = 0; i < cars.size(); i++){
            //init
            f = new CarFragment();
            b = new Bundle();
            //arguments
            b.putInt("INDEX", i);
            b.putString("NAME", cars.get(i).getCarBrand());
            f.setArguments(b);
            //adding to collection
            carFragments.add(f);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Stop", "CALLED");
    }

    public void addNewCar(View v){
        replaceFragment(R.id.central_fragment, new AddNewCarFragment());
    }

    public void addNewService(View v){
        String serviceType = "";
        int viewId = v.getId();

        switch (viewId){
            case R.id.fab_repair:
                serviceType = "repair";
                break;
            case R.id.fab_fluid:
                serviceType = "fluid";
                break;
            case R.id.fab_filter:
                serviceType = "filter";
                break;
        }

        ServiceFragment f = new ServiceFragment();

        Bundle b = new Bundle();
        b.putString("type", serviceType);
        f.setArguments(b);

        replaceFragment(R.id.central_fragment, f);

        closeFabs();
    }


    
    public void deleteCar(View v){
        replaceFragment(R.id.central_fragment, new DeleteCarFragment());
    }

    private long getMiles(int currentItem) {
        String carName = cars.get(currentItem).getCarBrand();
        SharedPreferences miles = getSharedPreferences("miles", Context.MODE_PRIVATE);
        return miles.getLong(carName, 0);
    }

    public void showFuelsList(View v){
        FuelsListFragment fuelsListFragment = new FuelsListFragment();

        Bundle b = new Bundle();
        b.putString("name", cars.get(p.getCurrentItem()).getCarBrand());
        fuelsListFragment.setArguments(b);

        replaceFragment(R.id.central_fragment, fuelsListFragment);
    }

    public void showRepairsList(View v){
        repairsListFragment = new RepairsListFragment();
        replaceFragment(R.id.central_fragment, repairsListFragment);
    }

    public void showFluidsList(View v){
        fluidsListFragment = new FluidsListFragment();
        replaceFragment(R.id.central_fragment, fluidsListFragment);
    }

    public void showFiltersList(View v){
        filtersListFragment = new FiltersListFragment();
        replaceFragment(R.id.central_fragment, filtersListFragment);
    }

    public void showSetMileageActivity(View v){
        Intent intent = new Intent(this, SetMileageActivity.class);
        intent.putExtra("CarName", cars.get(pager.getCurrentItem()).getCarBrand());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){return;}
        int position = pager.getCurrentItem();
        long mileage = Long.valueOf(data.getStringExtra("mileage"));
        carFragments.get(position).setMileage(mileage);
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

    public void closeFabs(){
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

    class MyBackStackChangedListener implements FragmentManager.OnBackStackChangedListener{
        @Override
        public void onBackStackChanged() {
            if(fragmentManager.getBackStackEntryCount() == 0){
                findViewById(R.id.sp_fragment).setVisibility(View.VISIBLE);
            }else{
                findViewById(R.id.sp_fragment).setVisibility(View.INVISIBLE);
            }

            if(!cars.isEmpty()) {
                if(spendingsFragment == null){
                    spendingsFragment = new SpendingsFragment();
                    spendingsFragment.setSpendings(pager.getCurrentItem());
                }else{
                    spendingsFragment.setSpendings(pager.getCurrentItem());
                }
            }
        }
    }

    class FabFuelClicked implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            findViewById(R.id.sp_fragment).setVisibility(View.INVISIBLE);
            replaceFragment(R.id.central_fragment, new FuelUpFragment());

            //hide fabs
            closeFabs();
        }
    }

    class FabRepairClicked implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            findViewById(R.id.sp_fragment).setVisibility(View.INVISIBLE);
            replaceFragment(R.id.central_fragment, new ServiceFragment());
        }
    }

    class CarsPagerListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //calculate fuel spendings for car
            spendingsFragment.setSpendings(pager.getCurrentItem());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
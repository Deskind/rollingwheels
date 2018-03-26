package deskind.com.rollingwheels.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import java.util.List;
import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.Car;
import deskind.com.rollingwheels.fragments.FuelUpFragment;
import deskind.com.rollingwheels.fragments.RepairsFragment;
import deskind.com.rollingwheels.fragments.SpendingsFragment;


public class MnActivity extends AppCompatActivity {

    private static FloatingActionButton fabPlus, fabFuel, fabRepair, fabFluid, fabFilter;
    private static Animation open, close, clockwise, anticlockwise;

    private static boolean isFabOpen = false;

    public static FragmentManager fragmentManager;

    private static ViewPager pager;

    private static List<Car> cars;
    private static MnActivity.PagerAdapter pagerAdapter;

    //Fragments
    private static int centralFragment;
    private static Fragment currentFragment;
    private static SpendingsFragment spendingsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_const);

        //find views
        pager = (ViewPager)findViewById(R.id.pager);
        centralFragment = R.id.central_fragment;
        fabPlus = (FloatingActionButton)findViewById(R.id.fab_plus);
        fabFuel = (FloatingActionButton)findViewById(R.id.fab_fuel);
        fabRepair= (FloatingActionButton)findViewById(R.id.fab_repair);
        fabFluid = (FloatingActionButton)findViewById(R.id.fab_fluid);
        fabFilter= (FloatingActionButton)findViewById(R.id.fab_filter);

        //load animations
        open = getAnimation(R.anim.fab_open, this);
        close = getAnimation(R.anim.fab_close, this);
        clockwise = getAnimation(R.anim.rotate_clockwise, this);
        anticlockwise = getAnimation(R.anim.rotate_anticlockwise, this);

        //setting up tags for fab views for using them in on click event
        fabFuel.setTag(new FuelUpFragment());
        fabRepair.setTag(new RepairsFragment());

        //set up listeners for views
        fabFuel.setOnClickListener(new OnFabClicked());
        fabRepair.setOnClickListener(new OnFabClicked());
        fabPlus.setOnClickListener(new MnActivity.FabPlusClicked());

        //init objects
        fragmentManager = getSupportFragmentManager();
        pagerAdapter = new MnActivity.PagerAdapter(fragmentManager);

        //database interactions
        cars = DBUtility.getAppDatabase(this).getCarsDao().getAllCars();

        //assign adapter to pager
        pager.setAdapter(pagerAdapter);

        //set up spendings fragment
        if(currentFragment == null) {
            showFragment(new SpendingsFragment(), R.id.central_fragment);
        }
    }

    public static String getCurrentCar(){
        int position = pager.getCurrentItem();
        String carName = cars.get(position).getCarBrand();
        return carName;
    }

    public static void setCurrentFragment(Fragment fragment){
        currentFragment = fragment;
    }

    private static void showFragment(Fragment f, int containerId){
        setCurrentFragment(f);
        fragmentManager.beginTransaction().add(containerId, f).commit();
    }

    public static void repalaceFragment(int containerId, Fragment f){
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
            repalaceFragment(R.id.central_fragment, (Fragment) view.getTag());
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

    //NESTED  CLASSES
    //Fragments
    public static class CarFragment extends Fragment {

        static final String ARGUMENT = "argument";

        public int index;

        public static MnActivity.CarFragment newInstance(int index){
            MnActivity.CarFragment fragment = new MnActivity.CarFragment();
            Bundle args = new Bundle();
            args.putInt(ARGUMENT, index);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.index = getArguments().getInt(ARGUMENT);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup)getLayoutInflater().inflate(R.layout.car_fragment, container, false);
            TextView carName = (TextView)rootView.findViewById(R.id.tv_car_name);
            String name = cars.get(index).getCarBrand();
            carName.setText(name);
            return rootView;
        }
    }

    //Adapters
    public static class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return MnActivity.CarFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return cars.size();
        }
    }

}

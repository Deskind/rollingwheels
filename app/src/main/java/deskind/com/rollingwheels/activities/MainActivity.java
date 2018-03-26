package deskind.com.rollingwheels.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.dao.CarsDAO;
import deskind.com.rollingwheels.database.DBUtility;
import deskind.com.rollingwheels.entities.Car;
import deskind.com.rollingwheels.fragments.FuelUpFragment;
import deskind.com.rollingwheels.fragments.SpendingsFragment;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fabPlus, fabFuel, fabRepair, fabFluid, fabFilter;
    Animation open, close, clockwise, anticlockwise;

    private static ViewPager pager;

    boolean isFabOpen = false;
    private static List<Car> cars;
    private static PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fab buttons
        fabPlus = (FloatingActionButton)findViewById(R.id.fab_plus);
        fabFuel = (FloatingActionButton)findViewById(R.id.fab_fuel);
        fabRepair = (FloatingActionButton)findViewById(R.id.fab_repair);
        fabFluid = (FloatingActionButton)findViewById(R.id.fab_fluid);
        fabFilter = (FloatingActionButton)findViewById(R.id.fab_filter);

        pager = (ViewPager)findViewById(R.id.pager);

        //init objects
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        //load animations
        open = getAnimation(R.anim.fab_open, this);
        close = getAnimation(R.anim.fab_close, this);
        clockwise = getAnimation(R.anim.rotate_clockwise, this);
        anticlockwise = getAnimation(R.anim.rotate_anticlockwise, this);

//        set listeners
        fabPlus.setOnClickListener(new FabClick());

        fabRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFabs();
                FuelUpFragment fuelUpFragment = new FuelUpFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.main_central, fuelUpFragment);
                ft.addToBackStack(null);

                ft.commit();
            }
        });

        //database interactions
        cars = DBUtility.getAppDatabase(this).getCarsDao().getAllCars();

        //assign adapter to pager
        pager.setAdapter(pagerAdapter);

        //pager listeners
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                Log.i("CURRENT CAR IS: " , cars.get(pager.getCurrentItem()).getCarBrand());
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        //set up spendings fragment
        SpendingsFragment spendingsFragment = new SpendingsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_central, spendingsFragment).commit();
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

    private Animation getAnimation(int animId, Context context){
        return AnimationUtils.loadAnimation(context, animId);
    }

    public static int carsCount(){return cars.size();}



    //inner classes
    class FabClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(isFabOpen){
                closeFabs();
            }else{
                openFabs();
            }
        }
    }

    public static class CarFragment extends Fragment{

        static final String ARGUMENT = "argument";

        public int index;

        public static CarFragment newInstance(int index){
            CarFragment fragment = new CarFragment();
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

    public static class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return CarFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return cars.size();
        }
    }
}

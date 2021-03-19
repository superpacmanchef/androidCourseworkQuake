package org.me.gcu.equakestartercode.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.me.gcu.equakestartercode.fragments.SerializableFragment;
import org.me.gcu.equakestartercode.viewmodels.ItemViewModel;
import org.me.gcu.equakestartercode.R;
import org.me.gcu.equakestartercode.fragments.HomeFragment;
import org.me.gcu.equakestartercode.fragments.MapFragment;
import org.me.gcu.equakestartercode.fragments.SearchFragment;
import org.me.gcu.equakestartercode.interfaces.IBottomNavMove;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener , IBottomNavMove , Serializable {
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment = null;
    private MapFragment mapFragment  = null;
    private SearchFragment searchFragment  = null;
    private ItemViewModel itemViewModel ;
    private String currentFragment = "Home";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
        {
            currentFragment = savedInstanceState.getString("current");
        }
            setContentView(R.layout.activity_main);
            bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
            bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
            itemViewModel = new ItemViewModel();
            itemViewModel.getItems();

            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("Home");
            searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag("Search");
            mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag("Map");

                if(homeFragment == null)
                {
                    homeFragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout , homeFragment , "Home").commit();
                    getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                }
                if(searchFragment == null)
                {
                    searchFragment = new SearchFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout , searchFragment , "Search").commit();
                    getSupportFragmentManager().beginTransaction().hide(searchFragment).commit();
                }
                if(mapFragment == null)
                {
                    mapFragment = new MapFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout , mapFragment , "Map").commit();
                    getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
                }

                if(currentFragment != null) {
                    if (currentFragment == homeFragment.getTag()) {
                        currentFragment = homeFragment.getTag();
                        getSupportFragmentManager().beginTransaction().show(homeFragment).commit();
                        getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
                        getSupportFragmentManager().beginTransaction().hide(searchFragment).commit();
                        getSupportFragmentManager().beginTransaction().addToBackStack("Home").commit();
                    } else if (currentFragment == searchFragment.getTag()) {
                        currentFragment = searchFragment.getTag();
                        getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                        getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
                        getSupportFragmentManager().beginTransaction().show(searchFragment).commit();
                        getSupportFragmentManager().beginTransaction().addToBackStack("Search").commit();
                    } else if (currentFragment == mapFragment.getTag()) {
                        currentFragment = mapFragment.getTag();
                        getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
                        getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                        getSupportFragmentManager().beginTransaction().hide(searchFragment).commit();
                        getSupportFragmentManager().beginTransaction().addToBackStack("Map").commit();
                    }
                }else{
                    currentFragment = homeFragment.getTag();
                    getSupportFragmentManager().beginTransaction().show(homeFragment).commit();
                    getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
                    getSupportFragmentManager().beginTransaction().hide(searchFragment).commit();
                    getSupportFragmentManager().beginTransaction().addToBackStack("Home").commit();
                }


            //load new items every 10 minuets
            final Handler handler = new Handler();
            Timer timer = new Timer();
            TimerTask doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            try {
                                itemViewModel.loadNewItems();
                            } catch (Exception e) {
                            }
                        }
                    });
                }
            };
            timer.schedule(doAsynchronousTask, 0, 60000); //execute in every 10 minutes
    }

    //Saves current Fragment tag when re-orientating
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("current" , currentFragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.hide(getSupportFragmentManager().findFragmentByTag(currentFragment));
        if(bottomNavigationView.getMenu().getItem(0).isChecked())
        {
            currentFragment = homeFragment.getTag();
            transaction.show(homeFragment);
            transaction.addToBackStack("Home");
        } else if(bottomNavigationView.getMenu().getItem(1).isChecked())
        {
            currentFragment = searchFragment.getTag();
            transaction.show(searchFragment);
            transaction.addToBackStack("Search");
        } else if(bottomNavigationView.getMenu().getItem(2).isChecked())
        {
            currentFragment = mapFragment.getTag();
            transaction.show(mapFragment);
            transaction.addToBackStack("Map");
        }
        transaction.commit();
        return false;
    }

    //On back press go backward thorough fragment stack
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    //On back press ensure that bottom nav check correlates to current fragment.
    @Override
    public void bottomNavMoved(String id) {
        if(id == "Home")
        {
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }
        else if(id == "Search")
        {
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
        }
        else if (id == "Map")
        {
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
        }

    }
}
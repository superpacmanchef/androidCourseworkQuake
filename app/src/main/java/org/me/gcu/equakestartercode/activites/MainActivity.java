package org.me.gcu.equakestartercode.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.me.gcu.equakestartercode.viewmodels.ItemViewModel;
import org.me.gcu.equakestartercode.R;
import org.me.gcu.equakestartercode.fragments.HomeFragment;
import org.me.gcu.equakestartercode.fragments.MapFragment;
import org.me.gcu.equakestartercode.fragments.SearchFragment;
import org.me.gcu.equakestartercode.interfaces.IBottomNavMove;


public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener , IBottomNavMove {
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment = null;
    private MapFragment mapFragment = null;
    private SearchFragment searchFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        ItemViewModel itemViewModel = new ItemViewModel();
        itemViewModel.getItems();
        this.onNavigationItemSelected(bottomNavigationView.getMenu().getItem(0));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(id == R.id.action_first_item)
        {

            if(homeFragment == null)
            {
                homeFragment = new HomeFragment();
            }
            ft.replace(R.id.frameLayout , homeFragment);
            ft.commit();
            ft.addToBackStack("Home");
        }
        else if(id == R.id.action_second_item)
        {

            if(searchFragment == null)
            {
                searchFragment = new SearchFragment();
            }
            ft.replace(R.id.frameLayout ,searchFragment);
            ft.commit();
            ft.addToBackStack("Search");
        }
        else if(id == R.id.action_third_item)
        {

            if(mapFragment == null)
            {
                mapFragment = new MapFragment();
            }
            ft.replace(R.id.frameLayout , mapFragment);
            ft.commit();
            ft.addToBackStack("Map");
        }
        return false;
    }

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
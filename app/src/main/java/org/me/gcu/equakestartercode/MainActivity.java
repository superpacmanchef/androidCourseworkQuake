package org.me.gcu.equakestartercode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.media.midi.MidiDeviceService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener; import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import javax.xml.xpath.*;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener , IBottomNavMove{
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment = null;
    private MapFragment mapFragment = null;
    private SearchFragment searchFragment = null;
    private ItemViewModel itemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        itemViewModel.getItems();
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(homeFragment == null)
        {
            homeFragment = new HomeFragment();
        }
        ft.add(R.id.frameLayout , homeFragment);
        ft.addToBackStack("Home");
        ft.commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        if(id == R.id.action_first_item)
        {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            if(homeFragment == null)
            {
             homeFragment = new HomeFragment();
            }
            ft.replace(R.id.frameLayout , homeFragment);
            ft.addToBackStack("Home");
            ft.commit();
        }
        else if(id == R.id.action_second_item)
        {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            if(searchFragment == null)
            {
                searchFragment = new SearchFragment();
            }
            ft.replace(R.id.frameLayout ,searchFragment);
            ft.addToBackStack("Search");
            ft.commit();
        }
        else if(id == R.id.action_third_item)
        {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            if(mapFragment == null)
            {
                mapFragment = new MapFragment();
            }
            ft.replace(R.id.frameLayout , mapFragment);
            ft.addToBackStack("Map");
            ft.commit();
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
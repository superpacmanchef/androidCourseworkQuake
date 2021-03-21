package org.me.gcu.equakestartercode.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.me.gcu.equakestartercode.interfaces.IBottomNavMove;
import org.me.gcu.equakestartercode.models.Item;
import org.me.gcu.equakestartercode.viewmodels.ItemViewModel;
import org.me.gcu.equakestartercode.R;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements  OnMapReadyCallback , CompoundButton.OnCheckedChangeListener {
    private List<Item> items ;
    private IBottomNavMove bottomNavMove;
    private ItemViewModel itemViewModel ;
    private SupportMapFragment mapFragment;
    private Button refresh;
    private CheckBox low;
    private CheckBox medium;
    private CheckBox high;
    private GoogleMap googleMap;
    private float zoom  = 5;
    private double latitude = 54.576519;
    private double longitude = -2.77954;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        low = (CheckBox) view.findViewById(R.id.low);
        medium = (CheckBox) view.findViewById(R.id.medium);
        high = (CheckBox) view.findViewById(R.id.high);


        if(savedInstanceState != null) {
            zoom = savedInstanceState.getFloat("zoom");
            latitude = savedInstanceState.getDouble("lat");
            longitude = savedInstanceState.getDouble("long");
            low.setChecked(savedInstanceState.getBoolean("low"));
            medium.setChecked(savedInstanceState.getBoolean(",medium"));
            high.setChecked(savedInstanceState.getBoolean("high"));
        }
        else{
            low.setChecked(true);
            medium.setChecked(true);
            high.setChecked(true);
        }

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        itemViewModel.getItems().observe(this  , listItems -> {
            items = listItems;
            getMap();
        });
        bottomNavMove = (IBottomNavMove) getActivity();

        low.setOnCheckedChangeListener(this);
        medium.setOnCheckedChangeListener(this);
        high.setOnCheckedChangeListener(this);
    }

    private void getMap() {
        if(googleMap != null) {
            zoom = googleMap.getCameraPosition().zoom;
            LatLng latLng = googleMap.getCameraPosition().target;
            latitude = latLng.latitude;
            longitude = latLng.longitude;
        }
        mapFragment.getMapAsync(this);
    }

    //Move Bottom Nav to correct position if not explicitly click on
    //Bottom Nav - i.e when pressing back
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden == false) {
            bottomNavMove.bottomNavMoved("Map");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(googleMap != null) {
            outState.putFloat("zoom", googleMap.getCameraPosition().zoom);
            LatLng latLng = googleMap.getCameraPosition().target;
            outState.putDouble("lat", latLng.latitude);
            outState.putDouble("long", latLng.longitude);
            outState.putBoolean("low" , low.isChecked());
            outState.putBoolean("medium" , medium.isChecked());
            outState.putBoolean("high",high.isChecked());
        }
    }

    private void addMarker(Item item , GoogleMap googleMap) {
        if (Float.parseFloat(item.getMagnitude()) < 1) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(item.getGeoLat()) , Double.parseDouble(item.getGeoLong()))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(item.getLocation()));
        } else if (Float.parseFloat(item.getMagnitude()) >= 1 && Float.parseFloat(item.getMagnitude()) < 3) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(item.getGeoLat()) , Double.parseDouble(item.getGeoLong()))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(item.getLocation()));
        } else if (Float.parseFloat(item.getMagnitude()) >= 3) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(item.getGeoLat()) , Double.parseDouble(item.getGeoLong()))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(item.getLocation()));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        for (int i = 0; i < items.size(); i++) {
            if(isCheckedItem(items.get(i))) {
                addMarker(items.get(i), googleMap);
            }
        }
            this.googleMap = googleMap;
            LatLng latLng = new LatLng(latitude, longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private boolean isCheckedItem(Item item) {
        float magnitude = Float.parseFloat(item.getMagnitude());
        if(low.isChecked() && magnitude < 1) {
            return true;
        }
        else if(medium.isChecked() && magnitude >= 1 && magnitude <3)
        {
            return true;
        }
        else if(high.isChecked() && magnitude > 3)
        {
            return true;
        }else{
            return false;
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getMap();
    }
}
package org.me.gcu.equakestartercode.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {
    private List<Item> items = new ArrayList<>();
    private IBottomNavMove bottomNavMove;
    private ItemViewModel itemViewModel ;
    private SupportMapFragment mapFragment;
    private OnMapReadyCallback callback = googleMap -> {
            for (int i = 0; i < items.size(); i++) {
                addMarker(items.get(i), googleMap);
            }
            LatLng latLng = new LatLng(54.576519, -2.77954);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    //TODO: ADD MARKER SEVERNESS CHECKBOX TO SHOW AND UNSHOW PINS
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        itemViewModel.getItems().observe(this  , listItems -> {
            items = listItems;
        });
        mapFragment.getMapAsync(callback);

        bottomNavMove = (IBottomNavMove) getActivity();
    }

    //Move Bottom Nav to correct position if not explcitly click on
    //Bottom Nav - i.e when pressing back
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden == false) {
            bottomNavMove.bottomNavMoved("Map");
        }
        itemViewModel.getItems().observe(this  , listItems -> {
            if(listItems != items)
            {   items = listItems;
                mapFragment.getMapAsync(callback);
            }
        });
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
}
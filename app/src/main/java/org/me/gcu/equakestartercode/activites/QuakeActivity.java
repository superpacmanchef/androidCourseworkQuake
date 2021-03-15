package org.me.gcu.equakestartercode.activites;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.me.gcu.equakestartercode.R;
import org.me.gcu.equakestartercode.models.Item;

public class QuakeActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private TextView locationText;
    private TextView dateText;
    private TextView latText;
    private TextView longText;
    private TextView depthText;
    private TextView magText;
    private TextView catagoryText;
    private TextView linkText;
    private Item selectedItem;
    private  MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quake_more);
        getSupportActionBar().setTitle("Quake Info");

        selectedItem = (Item)getIntent().getExtras().getSerializable("Item") ;
        locationText = (TextView)findViewById(R.id.locationText);
        locationText.setText(selectedItem.getLocation());
        dateText = (TextView)findViewById(R.id.dateText);
        dateText.setText(selectedItem.getPubDate());
        latText = (TextView)findViewById(R.id.latText);
        latText.setText(selectedItem.getGeoLat());
        longText = (TextView)findViewById(R.id.longText);
        longText.setText(selectedItem.getGeoLong());
        depthText = (TextView)findViewById(R.id.depthText);
        depthText.setText(selectedItem.getDepth());
        magText = (TextView)findViewById(R.id.magText);
        magText.setText(selectedItem.getMagnitude());
        catagoryText = (TextView)findViewById(R.id.catagoryText);
        catagoryText.setText(selectedItem.getCategory());
        linkText = (TextView)findViewById(R.id.linkText);
        linkText.setText(selectedItem.getLink());

        mapView = findViewById(R.id.itemLocation);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        LatLng latLng = new LatLng(Float.parseFloat(selectedItem.getGeoLat()) , Float.parseFloat(selectedItem.getGeoLong()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        addMarker(selectedItem , googleMap);
    }

    private void addMarker(Item item , GoogleMap googleMap)
    {
        if (Float.parseFloat(item.getMagnitude()) < 1) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(item.getGeoLat()) , Double.parseDouble(item.getGeoLong()))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(item.getLocation()));
        } else if (Float.parseFloat(item.getMagnitude()) >= 1 && Float.parseFloat(item.getMagnitude()) < 3) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(item.getGeoLat()) , Double.parseDouble(item.getGeoLong()))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(item.getLocation()));
        } else if (Float.parseFloat(item.getMagnitude()) >= 3) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(item.getGeoLat()) , Double.parseDouble(item.getGeoLong()))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(item.getLocation()));
        }
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}


package com.kstu.hw_5;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

// Импорты Mapbox v10
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils;
import com.mapbox.maps.plugin.locationcomponent.PuckBearing;

public class MapsActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapView = findViewById(R.id.mapView);
        mapView.getMapboxMap().loadStyle(Style.MAPBOX_STREETS, style -> {
            // Код для v10
            LocationComponentPlugin locationComponentPlugin = LocationComponentUtils.getLocationComponent(mapView);
            locationComponentPlugin.setEnabled(true);
            locationComponentPlugin.setPuckBearing(PuckBearing.COURSE);
        });
    }
}

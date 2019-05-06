package com.example.furkan.seproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class SelectStartEndActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private MapView mapView;
    private Marker marker;
    private Button button;
    private MapboxMap mapboxMap;

    public boolean isStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, MainActivity.mapBox_accessToken);
        setContentView(R.layout.activity_select_start_end);

        button = findViewById(R.id.select_btn);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        SelectStartEndActivity.this.mapboxMap = mapboxMap;
        mapboxMap.addOnMapClickListener(this);
        mapboxMap.getUiSettings().setAttributionEnabled(false);
        mapboxMap.getUiSettings().setLogoEnabled(false);
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        if (marker != null) {
            mapboxMap.removeMarker(marker);
        }
        String snippet = isStart ? "Start" : "End";

        marker = mapboxMap.addMarker(new MarkerOptions()
                .position(point)
                .snippet(snippet)
        );
        mapboxMap.selectMarker(marker);
        button.setVisibility(View.VISIBLE);
    }

    public void onClickBtn(View v) {
        if (isStart) {
            RouteFinder.getInstance().startPoint = marker.getPosition();
            isStart = false;
        }
        else {
            RouteFinder.getInstance().endPoint = marker.getPosition();
            isStart = true;

            Intent intent = new Intent(this, RouteListActivity.class);
            startActivity(intent);

            this.recreate();
        }
        mapboxMap.removeMarker(marker);
        button.setVisibility(View.INVISIBLE);
    }

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
        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this);
        }
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
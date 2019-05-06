package com.example.furkan.seproject;

import android.graphics.Color;

import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RouteFinder {

    private static RouteFinder single_instance = null;
    public LatLng startPoint, endPoint;
    private JSONObject json;
    public int pickedRoute = 0;

    private RouteFinder() {}

    public static RouteFinder getInstance() {
        if (single_instance == null)
            single_instance = new RouteFinder();
        return single_instance;
    }


    public JSONObject getJson() {
        return json;
    }

    public void findRoute() throws ExecutionException, InterruptedException {

        GetRoutesFromTrafi trafi = new GetRoutesFromTrafi(startPoint.getLatitude(), startPoint.getLongitude(), endPoint.getLatitude(), endPoint.getLongitude());
        this.json = trafi.getJson();

    }

    public void drawOnMap(JSONObject json, MapboxMap mapboxMap) throws JSONException {
        JSONObject route = json.getJSONArray("Routes").getJSONObject(pickedRoute);
        JSONArray routeSegments = route.getJSONArray("RouteSegments");

        for (int i = 0; i < routeSegments.length(); i++) {
            JSONObject segment = routeSegments.getJSONObject(i);
            String shape = segment.getString("Shape");
            List<LatLng> polylineList = PolylineEncoding.decode(shape);
            PolylineOptions polylineOptions = new PolylineOptions().addAll(polylineList).width(2.5f);

            if (segment.getInt("RouteSegmentType") == 1)
                polylineOptions.color(Color.RED);
            else
                polylineOptions.color(Color.BLUE);

            mapboxMap.addPolyline(polylineOptions);
        }

        CameraPosition.Builder builder = new CameraPosition.Builder();
        builder.target(startPoint);
        builder.zoom(11);
        mapboxMap.setCameraPosition(builder.build());

    }
}

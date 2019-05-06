package com.example.furkan.seproject;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class GetRoutesFromTrafi{

    private double start_lat;
    private double start_lng;
    private double end_lat;
    private double end_lng;
    static final String trafi_api_key = "9545ccea0fc0e6ba1f46aa461b01f485";

    GetRoutesFromTrafi(double start_lat, double start_lng, double end_lat, double end_lng) {
        this.start_lat = start_lat;
        this.start_lng = start_lng;
        this.end_lat = end_lat;
        this.end_lng = end_lng;
    }

    JSONObject getJson() throws ExecutionException, InterruptedException {
        String url = "http://api-ext.trafi.com/routes?start_lat=" + start_lat + "&start_lng=" + start_lng + "&end_lat=" + end_lat +"&end_lng=" + end_lng + "&api_key=" + trafi_api_key;
        AsyncTask task = new JsonReader().execute(url);
        JSONObject json = (JSONObject)task.get();
        return json;
    }

}
package com.stylingandroid.location.services;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

import hugo.weaving.DebugLog;

public class LocationFragment extends Fragment {
    private static final String FRACTIONAL_FORMAT = "%.4f";
    private static final String ACCURACY_FORMAT = "%.1fm";

    private TextView latitudeValue;
    private TextView longitudeValue;
    private TextView accuracyValue;

    private FusedLocationProviderClient fusedLocationProviderClient = null;

    @Override
    public void onStart() {
        super.onStart();
        registerForLocationUpdates();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        latitudeValue = (TextView) view.findViewById(R.id.latitude_value);
        longitudeValue = (TextView) view.findViewById(R.id.longitude_value);
        accuracyValue = (TextView) view.findViewById(R.id.accuracy_value);
        return view;
    }

    @Override
    public void onStop() {
        unregisterForLocationUpdates();
        super.onStop();
    }

    void updatePosition(Location location) {
        String latitudeString = createFractionString(location.getLatitude());
        String longitudeString = createFractionString(location.getLongitude());
        String accuracyString = createAccuracyString(location.getAccuracy());
        latitudeValue.setText(latitudeString);
        longitudeValue.setText(longitudeString);
        accuracyValue.setText(accuracyString);
    }

    private String createFractionString(double fraction) {
        return String.format(Locale.getDefault(), FRACTIONAL_FORMAT, fraction);
    }

    private String createAccuracyString(float accuracy) {
        return String.format(Locale.getDefault(), ACCURACY_FORMAT, accuracy);
    }

    @DebugLog
    @SuppressLint("MissingPermission")
    void registerForLocationUpdates() {
        FusedLocationProviderClient locationProviderClient = getFusedLocationProviderClient();
        LocationRequest locationRequest = LocationRequest.create();
        Looper looper = Looper.myLooper();
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, looper);
    }

    @DebugLog
    @NonNull
    private FusedLocationProviderClient getFusedLocationProviderClient() {
        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        }
        return fusedLocationProviderClient;
    }

    @DebugLog
    void  unregisterForLocationUpdates() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @DebugLog
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location lastLocation = locationResult.getLastLocation();
            updatePosition(lastLocation);
        }
    };
}

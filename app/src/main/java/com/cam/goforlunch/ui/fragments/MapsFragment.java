package com.cam.goforlunch.ui.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cam.goforlunch.R;
import com.cam.goforlunch.ui.ViewModel.MapsViewModel;
import com.cam.goforlunch.ui.ViewModel.MapsViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.mapView)
    MapView mapView;
    private MapsViewModel viewModel;
    public static final String SHARED_PREF_CURRENT_LATITUDE = "SHARED_PREF_CURRENT_LATITUDE";
    public static final String SHARED_PREF_CURRENT_LONGITUDE = "SHARED_PREF_CURRENT_LONGITUDE";

    private GoogleMap googleMap;

    private FusedLocationProviderClient fusedLocationProviderClient;

    // Default location set to Paris if permission is not granted
    private final LatLng defaultLocation = new LatLng(48.8566, 2.3522);
    private static final int DEFAULT_ZOOM = 15;
    private static final int LOCATION_REQUEST_CODE = 101;
    private boolean locationPermissionGranted;

    // Last known location retrieved by the Fused Location Provider
    private Location lastKnownLocation;
    private double currentLatitude;
    private double currentLongitude;
    public static final String SHARED_PREF_CURRENT_LOCATION = "SHARED_PREF_CURRENT_LACATION";



    // Keys for storing fragment state
    private static final String KEY_LOCATION = "location";

    public void MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Retrieve location and camera position from saved instance state
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        ButterKnife.bind(this, view);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences prefs = this.getActivity().getSharedPreferences(SHARED_PREF_CURRENT_LOCATION, Context.MODE_PRIVATE);
        viewModel = new ViewModelProvider(this, MapsViewModelFactory.getFactoryMapsInstance()).get(MapsViewModel.class);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }


    }

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });


        // Ask for permission, get location and set position of the map, then execute Nearby Places request
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation(map);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String latitude = sharedPref.getString(MapsFragment.SHARED_PREF_CURRENT_LATITUDE, "");
        String longitude= sharedPref.getString(MapsFragment.SHARED_PREF_CURRENT_LONGITUDE, "");
        String location = latitude + "," + longitude;
        viewModel.fetchRestaurant(location);

        viewModel.getPositions().observe(getViewLifecycleOwner(), list ->{
            for (LatLng position : list) {
                googleMap.addMarker(new MarkerOptions()
                        .position(position));
            }
        } );

    }

    // Get current location of the device and position the map's camera
    private void getDeviceLocation(GoogleMap map) {

        if (locationPermissionGranted) {



            @SuppressLint("MissingPermission") Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(requireActivity(), task -> {

                if (task.isSuccessful()) {

                    // Set the map's camera
                    lastKnownLocation = task.getResult();
                    if (lastKnownLocation != null) {
                        currentLatitude = Objects.requireNonNull(lastKnownLocation).getLatitude();
                        currentLongitude = lastKnownLocation.getLongitude();
                    } else {
                        currentLatitude = 48.856613;
                        currentLongitude = 2.352222;
                    }
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(currentLatitude, currentLongitude), DEFAULT_ZOOM));


                    //enregistrer latitude et longitude dans sharedPreferencies
                    SharedPreferences prefs = this.getActivity().getPreferences( Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(SHARED_PREF_CURRENT_LATITUDE, String.valueOf(currentLatitude));
                    editor.putString(SHARED_PREF_CURRENT_LONGITUDE, String.valueOf(currentLongitude));
                    editor.apply();
                }
            });

        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    // Ask user for permission to use the device location
    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
    }

    // Check if permission is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        locationPermissionGranted = false;

        // If request is cancelled, the result arrays are empty
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }

    // Update the map's UI settings if location permission is granted
    @SuppressLint("MissingPermission")
    private void updateLocationUI() {

        if (googleMap == null) {
            return;
        }
        if (locationPermissionGranted) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            googleMap.setMyLocationEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            lastKnownLocation = null;
            getLocationPermission();
        }
    }


}

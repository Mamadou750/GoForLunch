package com.cam.goforlunch.ui.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cam.goforlunch.data.RestaurantRepository;
import com.cam.goforlunch.model.Restaurant;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsViewModel extends ViewModel {
    //REPOSITORIES
    private final RestaurantRepository mRestaurantRepository;

    //CONSTRUCTOR
    public MapsViewModel (RestaurantRepository mRestaurantRepository) {
        this.mRestaurantRepository = mRestaurantRepository;

    }

    public LiveData<List<LatLng>> getPositions(){
        return this.mRestaurantRepository.getPositions();

    }

    public void fetchRestaurant(String location){
        this.mRestaurantRepository.executeSearchNearbyPlacesRequest(location);
    }

}

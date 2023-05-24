package com.cams.goforlunch.ui.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cams.goforlunch.data.RestaurantRepository;
import com.google.android.gms.maps.model.LatLng;

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

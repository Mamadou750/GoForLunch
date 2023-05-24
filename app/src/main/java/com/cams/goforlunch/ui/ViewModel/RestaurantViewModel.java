package com.cams.goforlunch.ui.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cams.goforlunch.data.RestaurantRepository;
import com.cams.goforlunch.model.Restaurant;

import java.util.List;

public class RestaurantViewModel extends ViewModel {

    //REPOSITORIES
    private final RestaurantRepository mRestaurantRepository;

    //CONSTRUCTOR
    public RestaurantViewModel(RestaurantRepository mRestaurantRepository) {
        this.mRestaurantRepository = mRestaurantRepository;

    }

    public LiveData<List<Restaurant>> getRestaurants(){
        return this.mRestaurantRepository.getRestaurants();
    }

    public void fetchRestaurant(String location){
        this.mRestaurantRepository.executeSearchNearbyPlacesRequest(location);
    }

}
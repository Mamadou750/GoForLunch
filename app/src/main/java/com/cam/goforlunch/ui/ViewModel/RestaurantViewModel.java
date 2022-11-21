package com.cam.goforlunch.ui.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cam.goforlunch.data.RestaurantRepository;
import com.cam.goforlunch.model.Restaurant;

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

}
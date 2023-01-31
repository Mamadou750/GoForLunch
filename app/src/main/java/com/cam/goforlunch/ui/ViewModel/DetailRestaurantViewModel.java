package com.cam.goforlunch.ui.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cam.goforlunch.data.DetailsRepository;
import com.cam.goforlunch.data.RestaurantRepository;
import com.cam.goforlunch.model.Restaurant;

import java.util.List;

public class DetailRestaurantViewModel extends ViewModel {

    //REPOSITORIES
    private final DetailsRepository mDetailsRepository;

    //CONSTRUCTOR
    public DetailRestaurantViewModel(DetailsRepository mDetailsRepository) {
        this.mDetailsRepository = mDetailsRepository;

    }

    public LiveData<Restaurant> getRestaurant(){
        return this.mDetailsRepository.getRestaurant();
    }

    public void fetchRestaurantDetails(Restaurant restaurant, String placeId){
        this.mDetailsRepository.executePlacesDetailsRequest(restaurant, placeId);
    }
}

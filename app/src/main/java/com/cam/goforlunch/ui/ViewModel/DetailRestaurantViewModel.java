package com.cam.goforlunch.ui.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cam.goforlunch.data.DetailsRepository;
import com.cam.goforlunch.data.UserRepository;
import com.cam.goforlunch.model.Restaurant;
import com.cam.goforlunch.model.User;

import java.util.List;

public class DetailRestaurantViewModel extends ViewModel {

    //REPOSITORIES
    private final DetailsRepository mDetailsRepository;
    private final UserRepository mUserRepository;

    //CONSTRUCTOR
    public DetailRestaurantViewModel(DetailsRepository mDetailsRepository, UserRepository mUserRepository) {
        this.mUserRepository = mUserRepository;
        this.mDetailsRepository = mDetailsRepository;


    }

    public LiveData<Restaurant> getRestaurant(){
        return this.mDetailsRepository.getRestaurant();
    }
    public LiveData<List<User>> getChousenUsers(Restaurant restaurant){
        return this.mDetailsRepository.getChousenUsers(restaurant);
    }

    public void fetchRestaurantDetails(Restaurant restaurant, String placeId){
        this.mDetailsRepository.executePlacesDetailsRequest(restaurant, placeId);
    }

    public void addRemoveFromList(boolean isRemove){
        mDetailsRepository.addOrRemoveFromList(isRemove);
    }




}
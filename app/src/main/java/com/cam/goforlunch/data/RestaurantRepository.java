package com.cam.goforlunch.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cam.goforlunch.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantRepository {

    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>(new ArrayList<>());

    public RestaurantRepository() {

        generateRandomRestaurants();

    }

    public LiveData<List<Restaurant>> getRestaurants(){
        return restaurantsLiveData;
    }


    public void addRestaurant(
            String name,
            String address,
            double rating,
            double latitude,
            double longitude,
            int distance,
            int nbWorkmates,
            String phoneNumber,
            String website,
            String isOpenNow,
            String imageUrl,
            String placeId
    ) {
        List<Restaurant> restaurants = restaurantsLiveData.getValue();

        if (restaurants == null) return;

        restaurants.add(
                new Restaurant (
                        name,
                        address,
                        rating,
                        latitude,
                        longitude,
                        distance,
                        nbWorkmates,
                        phoneNumber,
                        website,
                        isOpenNow,
                        imageUrl,
                        placeId
                )
        );

        restaurantsLiveData.setValue(restaurants);
    }



    public  void generateRandomRestaurants() {
        addRestaurant(

                "restau1",
                "adrrr",
                3,
                15,
                20,
                150,3,
                "mm","eee",
                "bb","kkk","plc"

        );
        addRestaurant(

                "restau 2",
                "adrrr",
                3,
                15,
                20,
                150,3,
                "mm","eee",
                "bb","kkk","plc"
        );

    }
}

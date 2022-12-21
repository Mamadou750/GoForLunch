package com.cam.goforlunch.data;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cam.goforlunch.BuildConfig;
import com.cam.goforlunch.data.api.GoogleAPIStreams;
import com.cam.goforlunch.model.GooglePlaces;
import com.cam.goforlunch.model.Restaurant;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class RestaurantRepository {

    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<LatLng>> positionsLiveData = new MutableLiveData<>(new ArrayList<>());

    public RestaurantRepository() {

    }

    public LiveData<List<Restaurant>> getRestaurants(){
        return restaurantsLiveData;
    }
    public LiveData<List<LatLng>> getPositions(){
        return positionsLiveData;
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


    private void createRestaurantList(List<GooglePlaces.Result> results) {

        List<Restaurant> restaurantList = new ArrayList<>();
        List<LatLng> positionList = new ArrayList<>();
        for (GooglePlaces.Result result : results) {

            Restaurant restaurant = new Restaurant().createRestaurantfromAPIResults(result);
            //executePlacesDetailsRequest(restaurant, restaurant.getPlaceId());
            //restaurant.setDistance(configureDistance(restaurant));
            //configureWorkmatesNumber(restaurant);
            restaurantList.add(restaurant);
            positionList.add(new LatLng(restaurant.getLatitude(), restaurant.getLongitude()));
        }
        restaurantsLiveData.setValue(restaurantList);
        positionsLiveData.setValue(positionList);

    }


   // Execute HTTP request to retrieve nearby places
    public void executeSearchNearbyPlacesRequest(String location) {

        String type = "restaurant";
        String key = BuildConfig.MAPS_API_KEY;

        GoogleAPIStreams.streamFetchPlaces(key, type, location, 5000).subscribeWith(
                new DisposableObserver<GooglePlaces>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onNext(GooglePlaces googlePlaces) {

                        createRestaurantList(googlePlaces.getResults());

                        Log.e("TAG", "On Next");
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("TAG", "On Error" + Log.getStackTraceString(e));
                    }

                    @Override
                    public void onComplete() {

                        Log.e("TAG", "On Complete");
                    }
                });
    }



}

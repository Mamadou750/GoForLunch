package com.cams.goforlunch.data;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cams.goforlunch.BuildConfig;
import com.cams.goforlunch.data.api.GoogleAPIStreams;
import com.cams.goforlunch.model.GooglePlaces;
import com.cams.goforlunch.model.Restaurant;
import com.cams.goforlunch.model.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.observers.DisposableObserver;

public class RestaurantRepository {

    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<LatLng>> positionsLiveData = new MutableLiveData<>(new ArrayList<>());
    List<User> workmatesList = new ArrayList<>();

    public RestaurantRepository() {

    }

    public LiveData<List<Restaurant>> getRestaurants(){
        return restaurantsLiveData;
    }
    public LiveData<List<LatLng>> getPositions(){
        return positionsLiveData;
    }


    private void createRestaurantList(List<GooglePlaces.Result> results) {

        List<Restaurant> restaurantList = new ArrayList<>();
        List<LatLng> positionList = new ArrayList<>();
        for (GooglePlaces.Result result : results) {

            Restaurant restaurant = new Restaurant().createRestaurantfromAPIResults(result);
            //executePlacesDetailsRequest(restaurant, restaurant.getPlaceId());
            //restaurant.setDistance(configureDistance(restaurant));

            configureWorkmatesNumber(restaurant);
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
                        UserRepository.getAllUsers().addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {
                                workmatesList.clear();
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                    User createUser = document.toObject(User.class);

                                    workmatesList.add(createUser);

                                }
                                createRestaurantList(googlePlaces.getResults());
                            }
                        });



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

    // Get number of users who have chosen the restaurant
    private void configureWorkmatesNumber(Restaurant restaurant) {

    int nbUser = 0;
      for (User user : workmatesList) {
        if (user.getChosenRestaurant() != null && user.getChosenRestaurant().getPlaceId().equals(restaurant.getPlaceId())) {
            nbUser ++;

        }
      }
      restaurant.setNbWorkmates(nbUser);
    }





}

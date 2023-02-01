package com.cam.goforlunch.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cam.goforlunch.BuildConfig;
import com.cam.goforlunch.data.api.GoogleAPIStreams;
import com.cam.goforlunch.model.GooglePlacesDetails;
import com.cam.goforlunch.model.Restaurant;

import io.reactivex.observers.DisposableObserver;

public class DetailsRepository {

    private final MutableLiveData <Restaurant> restaurantLiveData = new MutableLiveData<>();


    public DetailsRepository() {

    }

    public LiveData<Restaurant> getRestaurant(){
        return restaurantLiveData;
    }


    // Execute HTTP request to retrieve more information from the place
    public void executePlacesDetailsRequest(Restaurant restaurant, String placeId) {

        String key = BuildConfig.MAPS_API_KEY;

        GoogleAPIStreams.streamFetchPlacesDetails(key, placeId).subscribeWith(
                new DisposableObserver<GooglePlacesDetails>() {

                    @Override
                    public void onNext(GooglePlacesDetails googlePlacesDetails) {

                        restaurant.addDataFromDetailsRequest(restaurant, googlePlacesDetails.getResult());

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ereeur","ff");
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}


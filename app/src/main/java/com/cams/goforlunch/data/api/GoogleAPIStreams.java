package com.cams.goforlunch.data.api;

import com.cams.goforlunch.model.GooglePlaces;
import com.cams.goforlunch.model.GooglePlacesDetails;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

// Make HTTP requests on Google Places API
public interface GoogleAPIStreams {

    static Observable<GooglePlaces> streamFetchPlaces(String key, String type, String location, int radius) {

        GoogleAPIService googleAPIService = GoogleAPIService.retrofitGooglePlaces.create(GoogleAPIService.class);

        return googleAPIService.getPlaces(location, type, radius, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(100, TimeUnit.SECONDS);
    }

   static Observable<GooglePlacesDetails> streamFetchPlacesDetails(String key, String placeId) {

        GoogleAPIService googleAPIService = GoogleAPIService.retrofitGooglePlaces.create(GoogleAPIService.class);

        return googleAPIService.getPlacesDetails(key, placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(100, TimeUnit.SECONDS);
    }
}

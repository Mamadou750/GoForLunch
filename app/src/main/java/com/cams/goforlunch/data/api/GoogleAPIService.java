package com.cams.goforlunch.data.api;

import com.cams.goforlunch.BuildConfig;
import com.cams.goforlunch.model.GooglePlaces;
import com.cams.goforlunch.model.GooglePlacesDetails;


import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface GoogleAPIService {

    @GET("api/place/nearbysearch/json?sensor=true&key=" + BuildConfig.MAPS_API_KEY)
    Observable<GooglePlaces> getPlaces(@Query("location") String location,
                                       @Query("type") String type,
                                       @Query("radius") int radius,
                                       @Query("key") String key);

    Retrofit retrofitGooglePlaces = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    @GET("api/place/details/json?key=" + BuildConfig.MAPS_API_KEY)
    Observable<GooglePlacesDetails> getPlacesDetails(@Query("key") String key,
                                                     @Query("place_id") String placeId);


}

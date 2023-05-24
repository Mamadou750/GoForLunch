package com.cams.goforlunch.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cams.goforlunch.BuildConfig;
import com.cams.goforlunch.data.api.GoogleAPIStreams;
import com.cams.goforlunch.model.GooglePlacesDetails;
import com.cams.goforlunch.model.Restaurant;
import com.cams.goforlunch.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class DetailsRepository {

    private final MutableLiveData <Restaurant> restaurantLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<User>> usersChousenLiveData = new MutableLiveData<>(new ArrayList<>());
    private  RestaurantRepository restaurantRepository = new RestaurantRepository();
    List<User> workmatesList = new ArrayList<>();
    List<User> allWorkmatesList = new ArrayList<>();


    public DetailsRepository() {

    }

    public LiveData<Restaurant> getRestaurant(){
        return restaurantLiveData;
    }
    private static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    public LiveData<List<User>> getChousenUsers(Restaurant restaurant){
        getUsersCollection().get().addOnCompleteListener(task -> {
            workmatesList.clear();
            allWorkmatesList.clear();
            if (task.isSuccessful()){
                allWorkmatesList.addAll(task.getResult().toObjects(User.class));
                for (User user :task.getResult().toObjects(User.class) ) {


                    if (user.getChosenRestaurant() != null && user.getChosenRestaurant().getPlaceId().equals(restaurant.getPlaceId())) {
                        workmatesList.add(user);

                    }
                }
                usersChousenLiveData.setValue(workmatesList);
            }else {
                Log.d("erreur","erreur");
            }
        });
        return usersChousenLiveData;
    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    public void addOrRemoveFromList (boolean isRemove){
        User user= new User();
        user.setUsername(getCurrentUser().getDisplayName());
        if (isRemove){
            workmatesList.remove(user);
        }
        else {

            workmatesList.add(user);
        }
        usersChousenLiveData.setValue(workmatesList);
    }



    // Execute HTTP request to retrieve more information from the place
    public void executePlacesDetailsRequest(Restaurant restaurant, String placeId) {

        String key = BuildConfig.MAPS_API_KEY;

        GoogleAPIStreams.streamFetchPlacesDetails(key, placeId).subscribeWith(
                new DisposableObserver<GooglePlacesDetails>() {

                    @Override
                    public void onNext(GooglePlacesDetails googlePlacesDetails) {



                        restaurant.addDataFromDetailsRequest(restaurant, googlePlacesDetails.getResult());
                        restaurantLiveData.setValue(restaurant);

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


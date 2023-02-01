package com.cam.goforlunch.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cam.goforlunch.model.Restaurant;
import com.cam.goforlunch.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>(new ArrayList<>());


    public UserRepository() {



    }

    public LiveData<List<User>> getUsers(){
        return usersLiveData;
    }







    // COLLECTION REFERENCE
    private static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    // CREATE
    public static void createUser(String uid, String username, String urlPicture, Restaurant restaurant,
                                  List<Restaurant> likedRestaurants, boolean isEnabled) {
        User userToCreate = new User(uid, username, urlPicture, restaurant, likedRestaurants, isEnabled);
        UserRepository.getUsersCollection().document(uid).set(userToCreate);
    }

    // GET
    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserRepository.getUsersCollection().document(uid).get();
    }

    public static Task<QuerySnapshot> getAllUsers() {
        return UserRepository.getUsersCollection().get();
    }

    // UPDATE
    public static Task<Void> addLikedRestaurant(String uid, Restaurant restaurant) {
        return UserRepository.getUsersCollection().document(uid).update("likedRestaurants",
                FieldValue.arrayUnion(restaurant));
    }

    public static Task<Void> updateChosenRestaurant(String uid, Restaurant restaurant) {
        return UserRepository.getUsersCollection().document(uid).update("chosenRestaurant", restaurant);
    }

    public static void updateNotificationChoice(String uid, boolean isEnabled) {
        UserRepository.getUsersCollection().document(uid).update("notificationsEnabled", isEnabled);
    }


    // DELETE
    public static Task<Void> removeLikedRestaurant(String uid, Restaurant restaurant) {
        return UserRepository.getUsersCollection().document(uid).update("likedRestaurants",
                FieldValue.arrayRemove(restaurant));
    }

    public static void deleteUser(String uid) {
        UserRepository.getUsersCollection().document(uid).delete();
    }

}

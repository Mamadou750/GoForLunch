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

    public static MutableLiveData<List<User>> getAllUsers(){
        FirebaseFirestore.getInstance().collection("users");
        MutableLiveData<List<User>> getUsers = new MutableLiveData<>();
        getUsersCollection().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                getUsers.setValue(task.getResult().toObjects(User.class));
            }else {
                getUsers.setValue(null);
            }
        });
        return getUsers;
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

    // GET
   /* public static Task<DocumentSnapshot> getUser(String uid) {
        String uId ;
        MutableLiveData<User> user = new MutableLiveData<>();
        getUsersCollection().document(uId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                user.setValue(documentSnapshot.toObject(User.class));
            }else {
                user.setValue(null);
            }
        });
        return user;


    }*/

    /*public static Task<QuerySnapshot> getAllUsers() {
        return UserRepository.getUsersCollection().get();
    }*/

    // UPDATE
    public static Task<Void> addLikedRestaurant(String uid, Restaurant restaurant) {
        return UserRepository.getUsersCollection().document(uid).update("likedRestaurants",
                FieldValue.arrayUnion(restaurant));
    }

    public static Task<Void> updateChosenRestaurant(String uid, Restaurant restaurant) {
        return UserRepository.getUsersCollection().document(uid).update("chosenRestaurant", restaurant);
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

package com.cam.goforlunch.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cam.goforlunch.model.Restaurant;
import com.cam.goforlunch.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>(new ArrayList<>());
    private RestaurantRepository restaurantRepository;

    public UserRepository() {

        generateRandomUsers();

    }

    public LiveData<List<User>> getUsers(){
        return usersLiveData;
    }

    public void addUser(
            String userId,
            String username,
            String urlPicture,
            Restaurant restaurant,
            List<Restaurant> likedRestaurants,
            boolean isEnabled
    ){
        List<User> users = usersLiveData.getValue();
        if (users == null) return;

        users.add(
                new User (
                        userId,
                        username,
                        urlPicture,
                        restaurant,
                        likedRestaurants,
                        isEnabled
                )
        );

        usersLiveData.setValue(users);
    }

    public  void generateRandomUsers() {
        List<Restaurant> likedRestau = new ArrayList<>();
        likedRestau.add(new Restaurant("restau1",
                "adrrr",
                3,
                15,
                20,
                150,3,
                "mm","eee",
                "bb","kkk","plc"));

        likedRestau.add(new Restaurant("restau2",
                "adrrr",
                3,
                15,
                20,
                150,3,
                "mm","eee",
                "bb","kkk","plc"));
        addUser(

                "12",
                "bill",
                "rep",
                likedRestau.get(0),
                likedRestau,
                false

        );
        addUser(

                "13",
                "carl",
                "rap",
                likedRestau.get(1),
                likedRestau,
                false
        );

    }

}

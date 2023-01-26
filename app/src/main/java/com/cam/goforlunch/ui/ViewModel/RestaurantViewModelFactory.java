package com.cam.goforlunch.ui.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cam.goforlunch.data.RestaurantRepository;
import com.cam.goforlunch.data.UserRepository;

public class RestaurantViewModelFactory implements ViewModelProvider.Factory {

    private static RestaurantViewModelFactory factory;


    public static RestaurantViewModelFactory getFactoryRestaurantInstance() {
        if (factory == null) {
            synchronized (RestaurantViewModelFactory.class) {
                if (factory == null) {
                    factory = new RestaurantViewModelFactory(
                            new RestaurantRepository(

                            )
                    );
                }
            }
        }

        return factory;
    }



    // This field inherit the singleton property from the ViewModelFactory : it is scoped to the ViewModelFactory
    // Ask your mentor about DI scopes (Singleton, ViewModelScope, ActivityScope)
    @NonNull
    private final RestaurantRepository restaurantRepository;


    private RestaurantViewModelFactory(@NonNull RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }


    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantViewModel.class)) {
            return (T) new RestaurantViewModel(
                    restaurantRepository
            );
        } //else if (modelClass.isAssignableFrom(UserViewModel.class)) {
            //return (T) new UserViewModel(
                    //userRepository;
           //);
        //}
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
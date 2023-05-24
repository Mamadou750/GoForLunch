package com.cams.goforlunch.ui.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cams.goforlunch.data.RestaurantRepository;

public class MapsViewModelFactory implements ViewModelProvider.Factory {

    private static MapsViewModelFactory factory;


    public static MapsViewModelFactory getFactoryMapsInstance() {
        if (factory == null) {
            synchronized (MapsViewModelFactory.class) {
                if (factory == null) {
                    factory = new MapsViewModelFactory(
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


    private MapsViewModelFactory(@NonNull RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }


    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapsViewModel.class)) {
            return (T) new MapsViewModel(
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

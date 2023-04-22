package com.cam.goforlunch.ui.ViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cam.goforlunch.data.DetailsRepository;
import com.cam.goforlunch.data.RestaurantRepository;
import com.cam.goforlunch.data.UserRepository;

public class DetailRestaurantViewModelFactory implements ViewModelProvider.Factory  {
    private static DetailRestaurantViewModelFactory factoryDetailRestaurant;



    public static DetailRestaurantViewModelFactory getFactoryDetailsRestaurant() {
        if (factoryDetailRestaurant == null) {
            synchronized (DetailRestaurantViewModelFactory.class) {
                if (factoryDetailRestaurant == null) {
                    factoryDetailRestaurant = new DetailRestaurantViewModelFactory(
                            new DetailsRepository(

                            ),
                            new UserRepository(

                            )
                    );
                }
            }
        }

        return factoryDetailRestaurant;
    }

    // This field inherit the singleton property from the ViewModelFactory : it is scoped to the ViewModelFactory
    // Ask your mentor about DI scopes (Singleton, ViewModelScope, ActivityScope)

    @NonNull
    private final DetailsRepository detailsRepository;
    private final UserRepository userRepository;



    private DetailRestaurantViewModelFactory(@NonNull DetailsRepository detailsRepository, UserRepository userRepository) {
        this.detailsRepository = detailsRepository;
        this.userRepository = userRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DetailRestaurantViewModel.class)) {
            return (T) new DetailRestaurantViewModel(
                    detailsRepository,
                    userRepository
            );
        } //else if (modelClass.isAssignableFrom(UserViewModel.class)) {
        //return (T) new UserViewModel(
        //userRepository;
        //);
        //}
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

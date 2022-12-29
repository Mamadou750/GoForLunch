package com.cam.goforlunch.ui.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cam.goforlunch.data.RestaurantRepository;
import com.cam.goforlunch.data.UserRepository;

public class UserViewModelFactory implements ViewModelProvider.Factory {


        private static UserViewModelFactory factoryUser;



        public static UserViewModelFactory getFactoryUserInstance() {
            if (factoryUser == null) {
                synchronized (UserViewModelFactory.class) {
                    if (factoryUser == null) {
                        factoryUser = new UserViewModelFactory(
                                new UserRepository(

                                )
                        );
                    }
                }
            }

            return factoryUser;
        }

        // This field inherit the singleton property from the ViewModelFactory : it is scoped to the ViewModelFactory
        // Ask your mentor about DI scopes (Singleton, ViewModelScope, ActivityScope)

        @NonNull
        private final UserRepository userRepository;



        private UserViewModelFactory(@NonNull UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(WorksmatesViewModel.class)) {
                return (T) new WorksmatesViewModel(
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


package com.ncr.smartkart.network;

import com.ncr.smartkart.models.Item;
import com.ncr.smartkart.models.UserModel;
import com.ncr.smartkart.utils.EspressoIdlingResource;

public class Repository implements RemoteSource {

    private static Repository repository;
    private final RemoteDataSource remoteDataSource;

    public Repository(RemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static Repository getInstance(RemoteDataSource remoteDataSource) {
        if (repository == null) {
            repository = new Repository(remoteDataSource);
        }
        return repository;
    }

    @Override
    public void getItemDetails(String itemId, GetItemDetailsCallback getItemDetailsCallback) {
        EspressoIdlingResource.increment();
        remoteDataSource.getItemDetails(itemId, new GetItemDetailsCallback() {
            @Override
            public void onDataLoaded(Item item) {
                EspressoIdlingResource.decrement();
                getItemDetailsCallback.onDataLoaded(item);
            }

            @Override
            public void onDataError() {
                EspressoIdlingResource.decrement();
                getItemDetailsCallback.onDataError();
            }
        });
    }

    @Override
    public void startShopping(UserModel userModel, StartShoppingCallback startShoppingCallback) {
        EspressoIdlingResource.increment();
        remoteDataSource.startShopping(userModel, new StartShoppingCallback() {
            @Override
            public void onDataLoaded() {
                EspressoIdlingResource.decrement();
                startShoppingCallback.onDataLoaded();
            }

            @Override
            public void onDataError() {
                EspressoIdlingResource.decrement();
                startShoppingCallback.onDataError();
            }
        });
    }

    @Override
    public void stopShopping(String phoneNumber, StopShoppingCallback stopShoppingCallback) {
        EspressoIdlingResource.increment();
        remoteDataSource.stopShopping(phoneNumber, new StopShoppingCallback() {
            @Override
            public void onDataLoaded() {
                EspressoIdlingResource.decrement();
                stopShoppingCallback.onDataLoaded();
            }

            @Override
            public void onDataError() {
                EspressoIdlingResource.decrement();
                stopShoppingCallback.onDataError();
            }
        });
    }
}

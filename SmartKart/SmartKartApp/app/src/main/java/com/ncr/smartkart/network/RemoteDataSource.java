package com.ncr.smartkart.network;

import com.ncr.smartkart.models.Item;
import com.ncr.smartkart.models.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteDataSource implements RemoteSource {

    private static RemoteDataSource remoteDataSource;

    public static RemoteDataSource getInstance() {
        if (remoteDataSource == null) {
            remoteDataSource = new RemoteDataSource();
        }
        return remoteDataSource;
    }

    @Override
    public void getItemDetails(String itemId, GetItemDetailsCallback getItemDetailsCallback) {
        Call call = NetworkContext.apiSource.getItemDetails(itemId);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                getItemDetailsCallback.onDataLoaded((Item) response.body());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                getItemDetailsCallback.onDataError();
            }
        });
    }

    @Override
    public void startShopping(UserModel userModel, StartShoppingCallback startShoppingCallback) {
        Call call = NetworkContext.apiSource.startShopping(userModel);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                startShoppingCallback.onDataLoaded();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                startShoppingCallback.onDataError();
            }
        });
    }

    @Override
    public void stopShopping(String phoneNumber, StopShoppingCallback stopShoppingCallback) {
        Call call = NetworkContext.apiSource.stopShopping(phoneNumber);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                stopShoppingCallback.onDataLoaded();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                stopShoppingCallback.onDataError();
            }
        });
    }
}

package com.ncr.smartkart.network;

import com.ncr.smartkart.models.Item;
import com.ncr.smartkart.models.UserModel;

public interface RemoteSource {

    void getItemDetails(String itemId, GetItemDetailsCallback getItemDetailsCallback);

    void startShopping(UserModel userModel, StartShoppingCallback startShoppingCallback);

    void stopShopping(String phoneNumber, StopShoppingCallback stopShoppingCallback);

    interface GetItemDetailsCallback {
        void onDataLoaded(Item item);

        void onDataError();
    }

    interface StartShoppingCallback {
        void onDataLoaded();

        void onDataError();
    }

    interface StopShoppingCallback {
        void onDataLoaded();

        void onDataError();
    }
}

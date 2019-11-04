package com.ncr.smartkart;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ncr.smartkart.network.NetworkContext;
import com.ncr.smartkart.utils.PersistentDeviceStorage;

public class SmartKart extends MultiDexApplication {

    private static SmartKart smartKart;
    PersistentDeviceStorage persistentDeviceStorage;

    public static SmartKart getInstance() {
        return smartKart;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        smartKart = this;
        FirebaseApp.initializeApp(this);
        persistentDeviceStorage = PersistentDeviceStorage.getInstance(getApplicationContext());
        if (persistentDeviceStorage.getStoreUrl() != null) {
            networkContextInit();
        }
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult().getToken();
                persistentDeviceStorage.setFcm(token);
            }
        });
    }

    public void networkContextInit() {
        new NetworkContext().initialise();
    }
}

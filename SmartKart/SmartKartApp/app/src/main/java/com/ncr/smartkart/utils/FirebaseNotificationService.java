package com.ncr.smartkart.utils;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseNotificationService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseNotificationSer";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "onMessageReceived: " + remoteMessage.toString());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        PersistentDeviceStorage persistentDeviceStorage = PersistentDeviceStorage.getInstance(getApplicationContext());
        persistentDeviceStorage.setFcm(s);
    }
}

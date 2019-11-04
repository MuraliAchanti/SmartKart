package com.ncr.smartkart.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ncr.smartkart.models.Item;

import java.util.List;

/**
 * Created by pinna on 12/29/2017.
 */

public class PersistentDeviceStorage {
    private static PersistentDeviceStorage persistStorage = null;
    private static TinyDB tinyDB;
    private SharedPreferences sharedPreferences;

    private PersistentDeviceStorage(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public synchronized static PersistentDeviceStorage getInstance(Context context) {
        if (persistStorage == null) {
            persistStorage = new PersistentDeviceStorage(context);
        }
        if (tinyDB == null) {
            tinyDB = new TinyDB(context);
        }
        return persistStorage;
    }

    public String getEmail() {
        return sharedPreferences.getString(Constants.EMAIL_ID, "null");
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.EMAIL_ID, email);
        editor.apply();
    }

    public String getPhoneNumber() {
        return sharedPreferences.getString(Constants.PHONE_NUMBER, "null");
    }

    public void setPhoneNumber(String phoneNumber) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PHONE_NUMBER, phoneNumber);
        editor.apply();
    }

    public String getName() {
        return sharedPreferences.getString(Constants.NAME, "NoName");
    }

    public void setName(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.NAME, name);
        editor.apply();
    }

    public String getPic() {
        return sharedPreferences.getString(Constants.PROFILE_PIC, "null");
    }

    public void setPic(String pic) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PROFILE_PIC, pic);
        editor.apply();
    }

    public String getFcm() {
        return sharedPreferences.getString(Constants.FCM, "null");
    }

    public void setFcm(String fcm) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.FCM, fcm);
        editor.apply();
    }

    public void setOnGoingShopping(boolean onGoingShopping) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.ONGOING_SHOPPING, onGoingShopping);
        editor.apply();
    }

    public boolean getOngoingShopping() {
        return sharedPreferences.getBoolean(Constants.ONGOING_SHOPPING, false);
    }

    public List<Item> getShoppingList() {
        String jsonItems = sharedPreferences.getString(Constants.SHOPPING_LIST, null);
        if (jsonItems != null) {
            return new Gson().fromJson(jsonItems, new TypeToken<List<Item>>() {
            }.getType());
        }
        return null;
    }

    public void setShoppingList(List<Item> shoppingList) {
        Gson gson = new Gson();
        String jsonItems = gson.toJson(shoppingList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHOPPING_LIST, jsonItems);
        editor.apply();
    }

    public void removeShoppingList() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.SHOPPING_LIST);
        editor.remove(Constants.STORE_URL);
        editor.remove(Constants.STORE_NAME);
        editor.apply();
    }

    public String getStoreUrl() {
        return sharedPreferences.getString(Constants.STORE_URL, null);
    }

    public void setStoreUrl(String url) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.STORE_URL, url);
        editor.apply();
    }

    public String getStoreName() {
        return sharedPreferences.getString(Constants.STORE_NAME, null);
    }

    public void setStoreName(String storeName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.STORE_NAME, storeName);
        editor.apply();
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.EMAIL_ID);
        editor.remove(Constants.NAME);
        editor.remove(Constants.PROFILE_PIC);
        editor.remove(Constants.PHONE_NUMBER);
        editor.remove(Constants.FCM);
        editor.apply();
    }
}
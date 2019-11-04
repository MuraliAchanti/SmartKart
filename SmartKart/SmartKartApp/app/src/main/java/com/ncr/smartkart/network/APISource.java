package com.ncr.smartkart.network;

import com.ncr.smartkart.models.Item;
import com.ncr.smartkart.models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APISource {

    @GET("getitemdetails")
    Call<Item> getItemDetails(@Query("id") String id);

    @POST("addactiveuser")
    Call<Void> startShopping(@Body UserModel userModel);

    @GET("deleteactiveuser")
    Call<Void> stopShopping(@Query("id") String phoneNumber);
}

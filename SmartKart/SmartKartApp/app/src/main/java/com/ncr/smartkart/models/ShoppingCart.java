package com.ncr.smartkart.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ShoppingCart implements Serializable {
    String storeName;
    List<Item> shoppingList;

    public ShoppingCart() {
    }

    public ShoppingCart(String storeName, List<Item> shoppingList) {
        this.storeName = storeName;
        this.shoppingList = shoppingList;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<Item> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<Item> shoppingList) {
        this.shoppingList = shoppingList;
    }
}

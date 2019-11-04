package com.ncr.smartkart.models;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("_id")
    private String itemId;

    @SerializedName("weight")
    private Float weight;

    @SerializedName("price")
    private Float price;

    @SerializedName("name")
    private String itemName;

    @SerializedName("quantity")
    private Integer quantity;

    public Item(String itemId, Float weight, Float price, String itemName, Integer quantity) {
        this.itemId = itemId;
        this.weight = weight;
        this.price = price;
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public Item() {
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", weight=" + weight +
                ", price=" + price +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}

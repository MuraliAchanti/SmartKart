package com.ncr.smartkart.models;

import java.util.List;

public class GeneralItem extends ListItem {

    private String storeName;
    private List<Item> itemList;

    public GeneralItem(String storeName, List<Item> itemList) {
        this.storeName = storeName;
        this.itemList = itemList;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }
}

package com.ncr.smartkart.models;

public class UserModel {

    private String phoneNumber;
    private String name;
    private String email;
    private String fcm;
    private String photoUrl;

    public UserModel() {
    }

    public UserModel(String name, String email, String fcm, String photoUrl) {
        this.name = name;
        this.email = email;
        this.fcm = fcm;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", fcm='" + fcm + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}

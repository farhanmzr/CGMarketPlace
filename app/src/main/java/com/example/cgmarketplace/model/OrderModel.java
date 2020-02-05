package com.example.cgmarketplace.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class OrderModel {

    private String address, city, country, region, status, totalOrder, zipcode, name, phone;

    @ServerTimestamp
    private Date date;

    public  OrderModel(){}

    public OrderModel(String address, String city, String country, String region, String status, String totalOrder, String zipcode, String name, String phone, Date date) {
        this.address = address;
        this.city = city;
        this.country = country;
        this.region = region;
        this.status = status;
        this.totalOrder = totalOrder;
        this.zipcode = zipcode;
        this.date = date;
        this.name = name;
        this.phone = phone;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(String totalOrder) {
        this.totalOrder = totalOrder;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

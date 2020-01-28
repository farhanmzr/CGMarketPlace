package com.example.cgmarketplace.model;

public class CartModel {

    private String image, name, width, height, dense, material, finish;
    private int price, qty, total;

    public CartModel() {}

    public CartModel(String image, String name, String width, String height, String dense, String material, String finish, int price, int qty) {
        this.image = image;
        this.name = name;
        this.width = width;
        this.height = height;
        this.dense = dense;
        this.material = material;
        this.finish = finish;
        this.price = price;
        this.qty = qty;
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public String getDense() {
        return dense;
    }

    public String getMaterial() {
        return material;
    }

    public String getFinish() {
        return finish;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}

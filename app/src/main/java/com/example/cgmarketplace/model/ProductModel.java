package com.example.cgmarketplace.model;

public class ProductModel {

    private String desc, details1, details2, details3, details4, finish, image1, image2, image3, material, name, width, height, dense, category;
    private int price, stock;

    public  ProductModel(){}

    public ProductModel(String desc, String details1, String details2, String details3, String details4, String finish, String image1, String image2, String image3, String material, String name, String width, String height, String dense, String category, int price, int stock) {
        this.desc = desc;
        this.details1 = details1;
        this.details2 = details2;
        this.details3 = details3;
        this.details4 = details4;
        this.finish = finish;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.material = material;
        this.name = name;
        this.width = width;
        this.height = height;
        this.dense = dense;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDetails1() {
        return details1;
    }

    public void setDetails1(String details1) {
        this.details1 = details1;
    }

    public String getDetails2() {
        return details2;
    }

    public void setDetails2(String details2) {
        this.details2 = details2;
    }

    public String getDetails3() {
        return details3;
    }

    public void setDetails3(String details3) {
        this.details3 = details3;
    }

    public String getDetails4() {
        return details4;
    }

    public void setDetails4(String details4) {
        this.details4 = details4;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDense() {
        return dense;
    }

    public void setDense(String dense) {
        this.dense = dense;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}

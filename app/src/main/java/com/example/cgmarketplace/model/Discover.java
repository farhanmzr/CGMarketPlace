package com.example.cgmarketplace.model;

public class Discover {
    private String name;
    private String desc;
    private int photo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public Discover(String name, String desc, int photo) {
        this.name = name;
        this.desc = desc;
        this.photo = photo;
    }


}

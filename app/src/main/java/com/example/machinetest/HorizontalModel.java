package com.example.machinetest;

public class HorizontalModel {

    int id;
    String name;
    byte[] image;

    public HorizontalModel(String name, byte[] image) {
        this.name = name;
        this.image = image;
    }

    public HorizontalModel(int id, String name, byte[] image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

package com.example.machinetest;

public class VerticaLModel {

    String product_name;
    String category_name;
    byte[] product_image;
    String price;

    public VerticaLModel(String product_name, String category_name, byte[] product_image, String price) {
        this.product_name = product_name;
        this.category_name = category_name;
        this.product_image = product_image;
        this.price = price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public byte[] getProduct_image() {
        return product_image;
    }

    public void setProduct_image(byte[] product_image) {
        this.product_image = product_image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}

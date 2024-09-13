package com.capstone.foodineapp.Models;

import android.media.Image;

public class Product {


    /* Object class for the product, We created this object to help us store and display what we want for our product. It's necessary for the
     * real time database*/

    private String productName;
    private String description;


    public Product() {
    }

    public Product(String productName, String description) {
        this.productName = productName;
        this.description = description;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

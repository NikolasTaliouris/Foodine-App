package com.capstone.foodineapp.Models;

public class Food {


    /* Object class for the food , We created this object to help us store and display what we want for our food. It's necessary for the
     * real time database*/

    private String foodName;
    private String description;
    private String price;


    /*Default Constructor for displaying the Cart items in a recycler-viewer. We need it for retrieving data from real time database*/

    public Food() {
    }

    public Food(String foodName, String description, String price) {
        this.foodName = foodName;
        this.description = description;
        this.price = price;
    }

    public String getFoodName() {
        return foodName;
    }



    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}

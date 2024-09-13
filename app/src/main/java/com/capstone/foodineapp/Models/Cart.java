package com.capstone.foodineapp.Models;

import com.capstone.foodineapp.Models.Food;

/* Object class for the cart , We created this object to help us store and display what we want for our Cart. It's necessary for the
* real time database*/

public class Cart {

    //Takes everything that the Food object has
    private Food food;

    private String cartID;
    private int quantity;


    /*Default Constructor for displaying the Cart items in a recycler-viewer. We need it for retrieving data from real time database*/

    public Cart() {
    }

    public Cart(Food food,int quantity, String cartID) {
        this.food = food;
        this.quantity = quantity;
        this.cartID = cartID;
    }



    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }




    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

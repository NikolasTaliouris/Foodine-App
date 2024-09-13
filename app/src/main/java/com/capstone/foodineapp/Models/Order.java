package com.capstone.foodineapp.Models;

import java.util.ArrayList;

/* Object class for the food , We created this object to help us store and display what we want for our Order. It's necessary for the
 * real time database*/

public class Order {

    private String orderID;

    // Getting an arraylist for Cart objects. We need it because we want to create a history of user Orders
    private ArrayList<Cart> cartList;

    public Order() {
    }

    public Order(ArrayList<Cart> cartList) {
        this.cartList = cartList;
    }

    public ArrayList<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<Cart> cartList) {
        this.cartList = cartList;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }


}


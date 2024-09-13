package com.capstone.foodineapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.capstone.foodineapp.Models.Cart;
import com.capstone.foodineapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;


/*The code is implementing an adapter for a RecyclerView that displays a list of items.*/
public class MyAdapter3 extends RecyclerView.Adapter<MyAdapter3.MyViewHolder3>{

    Context context3;
    ArrayList<Cart> cartlist;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Cart");

    //The Adapter constructor -> a Context and an ArrayList of cart objects.
    // The Context is used to inflate the layout for each item in the onCreateViewHolder() method.
    public MyAdapter3(Context context3, ArrayList<Cart> cartlist) {
        this.context3 = context3;
        this.cartlist = cartlist;
    }


    @NonNull
    @Override
    public MyAdapter3.MyViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context3).inflate(R.layout.cartitem,parent,false);
        return new MyAdapter3.MyViewHolder3(view);
    }

    //The onBindViewHolder() method is called for each item in the RecyclerView,
    // and it binds the data from Cart object at the current position to the views in the MyViewHolder.
    @Override
    public void onBindViewHolder(@NonNull MyAdapter3.MyViewHolder3 holder, int position) {
        int itemPosition = holder.getAbsoluteAdapterPosition();
        Cart cart = cartlist.get(itemPosition);
        holder.cartName.setText(cart.getFood().getFoodName());
        holder.cartPrice.setText(cart.getFood().getPrice());
        holder.cartDescription.setText(cart.getFood().getDescription());
        updateTotalPrice();

        //The remove button, Removes the item from the shopping cart,
        //It updates the adapter and it removes the item from the database

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (itemPosition >= 0 && itemPosition < cartlist.size()) {
                            cartlist.remove(itemPosition);
                            notifyItemRemoved(itemPosition);
                            notifyItemRangeChanged(itemPosition, cartlist.size());
                            database.child(cart.getCartID()).removeValue();
                            Toast.makeText(context3, "Item has been successfully removed from the cart!", Toast.LENGTH_SHORT).show();
                            updateTotalPrice();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        //It increases the quantity of the item. and it updates the quantity in the firebase
        //updateTotalPrice method update the total price of all Items

        holder.incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart cart= cartlist.get(itemPosition);
                cart.setQuantity(cart.getQuantity() + 1);
                holder.cartQuantity.setText(String.valueOf(cart.getQuantity()));
                database.child(cart.getCartID()).child("quantity").setValue(cart.getQuantity());
                updateTotalPrice();
            }
        });

        //It decreases the quantity of the item. and it updates the quantity in the firebase
        //updateTotalPrice method update the total price of all Items

        holder.decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.getQuantity() > 1) {
                    Cart cart = cartlist.get(itemPosition);
                    cart.setQuantity(cart.getQuantity() - 1);
                    holder.cartQuantity.setText(String.valueOf(cart.getQuantity()));
                    database.child(cart.getCartID()).child("quantity").setValue(cart.getQuantity());
                    updateTotalPrice();
                }
            }
        });
    }

    //returns the number of items in the ArrayList.

    @Override
    public int getItemCount() {
        return cartlist.size();
    }

    // A method that calculates the total price of all item in the shopping cart,
    // and returns a totalPrice as a double.
    private double calculateTotalPrice(List<Cart> cartItems) {
        double totalPrice = 0.0;
        for (Cart cart : cartItems) {
            double itemPrice = Double.parseDouble(cart.getFood().getPrice());
            int itemQuantity = cart.getQuantity();
            totalPrice += itemPrice * itemQuantity;
        }
        return totalPrice;
    }

    //We use this method to update the total price in our shopping cart and displays the total price.
    private void updateTotalPrice() {
        double totalPrice = calculateTotalPrice(cartlist);
        Toast.makeText(context3, "Your total cost is :" + totalPrice + " EUR", Toast.LENGTH_SHORT).show();
    }

    // It defines the views for each item in the RecyclerView.
    // It has four TextViews for the cartName,cartDescription,cartPrice,cartQuantity.
    //And three buttons removeBtn,incrementButton,decrementButton, which remove the item in the shopping Cart, increase/decrease the quantity

    public static class MyViewHolder3 extends RecyclerView.ViewHolder{
        TextView cartName,cartDescription,cartPrice,cartQuantity;
        Button removeBtn,incrementButton,decrementButton;
        public MyViewHolder3(@NonNull View itemView) {
            super(itemView);
            cartName = itemView.findViewById(R.id.cartName);
            cartDescription = itemView.findViewById(R.id.cartDescription);
            cartPrice = itemView.findViewById(R.id.cartPrice);
            removeBtn = itemView.findViewById(R.id.removeItem);
            cartQuantity = itemView.findViewById(R.id.cartQuantity);
            incrementButton = itemView.findViewById(R.id.incrementQuantity);
            decrementButton = itemView.findViewById(R.id.decrementQuantity);
        }
    }
}

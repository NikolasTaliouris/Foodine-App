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
import com.capstone.foodineapp.Models.Food;
import com.capstone.foodineapp.Models.Order;
import com.capstone.foodineapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/*The code is implementing an adapter for a RecyclerView that displays a list of items.*/
public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder2>{


    Context context2;
    ArrayList<Food> foodlist;

    //The Adapter constructor -> a Context and an ArrayList of Item objects.
    // The Context is used to inflate the layout for each item in the onCreateViewHolder() method.

    public MyAdapter2(Context context, ArrayList<Food> foodlist) {
        this.context2 = context;
        this.foodlist = foodlist;
    }

    @NonNull
    @Override
    public MyAdapter2.MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context2).inflate(R.layout.fooditem,parent,false);
        return new MyViewHolder2(view);
    }

    //The onBindViewHolder() method is called for each item in the RecyclerView,
    // and it binds the data from Item object at the current position to the views in the MyViewHolder.

    @Override
    public void onBindViewHolder(@NonNull MyAdapter2.MyViewHolder2 holder, int position) {
        int itemPosition = holder.getAbsoluteAdapterPosition();
        Food food = foodlist.get(itemPosition);
        holder.foodName.setText(food.getFoodName());
        holder.description.setText(food.getDescription());
        holder.price.setText(food.getPrice());
        holder.setAddToCartClickListener(food);
    }

    //returns the number of items in the ArrayList.
    @Override
    public int getItemCount() {
        return foodlist.size();
    }


    //It takes a List of Items objects as an argument
    // and replaces the existing ArrayList with the filtered list.
    public void filterList2(List<Food> filteredList2){
        foodlist = (ArrayList<Food>) filteredList2;
        notifyDataSetChanged();
    }

    // It defines the views for each item in the RecyclerView.
    // It has three TextViews for the Item name,description and price.
    //And one button addToCart, which adds the item in the shopping Cart

    public static class MyViewHolder2 extends RecyclerView.ViewHolder{
        TextView foodName,description,price;
        Button addToCart;
        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.tvfoodName);
            description = itemView.findViewById(R.id.foodDescription);
            price = itemView.findViewById(R.id.foodPrice);
            addToCart = itemView.findViewById(R.id.addtoCart);
        }

        // It creates a new Cart object
        // generates a unique key for the cart item using push().getKey(),
        // and adds the cart item to the Cart in the database using setValue().
        //setValue and push.getKey -> It's from firebase
        public void setAddToCartClickListener(Food food) {
            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart");
                    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");
                    Cart cart = new Cart(food,1, cartRef.push().getKey()); // Add one item to cart
                    cartRef.child(cart.getCartID()).setValue(cart);
                    // Add food to shopping cart
                    Toast.makeText(itemView.getContext(), "Added " + food.getFoodName() + " to cart", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

package com.capstone.foodineapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.foodineapp.Models.Product;
import com.capstone.foodineapp.R;

import java.util.ArrayList;
import java.util.List;

/*The code is implementing an adapter for a RecyclerView that displays a list of products.*/

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    Context context;
    ArrayList<Product> list;

//The Adapter constructor -> a Context and an ArrayList of Product objects.
// The Context is used to inflate the layout for each item in the onCreateViewHolder() method.

    public MyAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(view);
    }

    //The onBindViewHolder() method is called for each item in the RecyclerView,
    // and it binds the data from Product object at the current position to the views in the MyViewHolder.

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
          int itemPosition = holder.getAbsoluteAdapterPosition();
          Product product = list.get(itemPosition);
          holder.productName.setText(product.getProductName());
          holder.description.setText(product.getDescription());
    }

    //returns the number of items in the ArrayList.
    @Override
    public int getItemCount() {
        return list.size();
    }


    //It takes a List of Product objects as an argument
    // and replaces the existing ArrayList with the filtered list.
    public void filterList(List<Product> filteredList){
        list = (ArrayList<Product>) filteredList;
        notifyDataSetChanged();
    }

    // It defines the views for each item in the RecyclerView.
    // It has two TextViews for the product name and description.

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView productName;
        TextView description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.tvproductName);
            description = itemView.findViewById(R.id.productDescription);

        }
    }

}

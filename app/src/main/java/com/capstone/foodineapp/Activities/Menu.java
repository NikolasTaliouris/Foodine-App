package com.capstone.foodineapp.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.foodineapp.Adapters.MyAdapter;
import com.capstone.foodineapp.Models.Product;
import com.capstone.foodineapp.Models.User;
import com.capstone.foodineapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;

    SearchView searchView;
    ArrayList<Product> list;




    //Initialize the searchview and the recyclerview

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.productList);
        database = FirebaseDatabase.getInstance().getReference("Products");

        //It sets the size of the RecyclerView to be fixed,
        //It means that the layout manager can optimize the layout by not measuring each item during layout. Better Performance
        recyclerView.setHasFixedSize(true);

        //It sets the layout manager for the RecyclerView.
        //A LinearLayoutManager is used to display items in a vertical scrolling list.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        //It creates an instance of the adapter for the RecyclerView.
        //The adapter acts as a bridge between the RecyclerView and the data source.
        myAdapter = new MyAdapter(this,list);

        //It sets the adapter for the RecyclerView,
        // The RecyclerView will use the adapter to display data.
        recyclerView.setAdapter(myAdapter);


        //We used this feature to help the user browse through the items of the selected category
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });


        //A way to press the category that we want and display the items of the selected category
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            User user = (User) getIntent().getSerializableExtra("user");
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && e.getAction() == MotionEvent.ACTION_UP) {

                    int position = rv.getChildAdapterPosition(childView);
                    String selectedProduct = String.valueOf(list.get(position).getProductName());
                    Intent intent = new Intent(Menu.this, FoodList.class);
                    intent.putExtra("Products", selectedProduct);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        //Retrieve the Product items from the database and add it inside of an Arraylist<Product>
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    list.add(product);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //This method iterates through the list<Product> with an if statement,
    //in order to help us find the category that we want in the search view.
    private void filter(String newText) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : list){
            if (product.getProductName().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(product);
            }
        }
        myAdapter.filterList(filteredList);
    }


    //The menu is inflated using the MenuInflater class.
    //It inflates the menu layout named main_menu.
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    /*The menu item selected by the user is identified by the getItemId() method.
      Depending on the menu item, an appropriate action is taken.
      */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        User user = (User) getIntent().getSerializableExtra("user");
        switch (item.getItemId()){
            case R.id.Logout:
                finish();
                Intent intent = new Intent(Menu.this,SignIn.class);
                startActivity(intent);
                return true;
            case R.id.profile:
                Intent intent2 = new Intent(Menu.this,ProfileManager.class);
                intent2.putExtra("user",user);
                startActivity(intent2);
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

}
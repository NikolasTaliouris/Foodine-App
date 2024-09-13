package com.capstone.foodineapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.capstone.foodineapp.Adapters.MyAdapter2;
import com.capstone.foodineapp.Models.Food;
import com.capstone.foodineapp.Models.User;
import com.capstone.foodineapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView2;
    FloatingActionButton btn_shoppingCart;
    DatabaseReference database;
    MyAdapter2 myAdapter2;
    SearchView searchView2;
    ArrayList<Food> foodlist;


    //In the onCreate method, the view elements are initialized by Id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        searchView2 = findViewById(R.id.searchView2);
        String selectedCategory = getIntent().getStringExtra("Products");
        recyclerView2 = findViewById(R.id.foodList);
        btn_shoppingCart = findViewById(R.id.btn_shoppingcart);


        database = FirebaseDatabase.getInstance().getReference("Food").child(selectedCategory);

        //It sets the size of the RecyclerView to be fixed,
        //It means that the layout manager can optimize the layout by not measuring each item during layout. Better Performance
        recyclerView2.setHasFixedSize(true);
        //It sets the layout manager for the RecyclerView.
        //A LinearLayoutManager is used to display items in a vertical scrolling list.
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        foodlist = new ArrayList<>();
        //It creates an instance of the adapter for the RecyclerView.
        //The adapter acts as a bridge between the RecyclerView and the data source.
        myAdapter2 = new MyAdapter2(this,foodlist);
        //It sets the adapter for the RecyclerView,
        // The RecyclerView will use the adapter to display data.
        recyclerView2.setAdapter(myAdapter2);


        //We used this feature to help the user browse through the items of the selected item
        searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText2) {
                filter2(newText2);
                return true;
            }
        });

        //Retrieve the food items from the database and add it inside of an Arraylist<Food>
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);
                    foodlist.add(food);
                }
                myAdapter2.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // a float button just to go from foodlist to the shopping cart activity

        btn_shoppingCart.setOnClickListener(new View.OnClickListener() {
            User user = (User) getIntent().getSerializableExtra("user");
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodList.this,ShoppingCart.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
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

        switch (item.getItemId()){
            case R.id.Logout:
                finish();
                Intent intent = new Intent(FoodList.this,SignIn.class);
                startActivity(intent);
                return true;
            case R.id.profile:
                User user = (User) getIntent().getSerializableExtra("user");
                Intent intent2 = new Intent(FoodList.this,ProfileManager.class);
                intent2.putExtra("user",user);
                startActivity(intent2);

        }
        return super.onOptionsItemSelected(item);
    }

    //This method iterates through the foodlist with an if statement,
    //in order to help us find the item that we want in the search view.
    private void filter2(String newText2) {
        List<Food> filteredList2 = new ArrayList<>();
        for (Food food : foodlist){
            if (food.getFoodName().toLowerCase().contains(newText2.toLowerCase())){
                filteredList2.add(food);
            }
        }
        myAdapter2.filterList2(filteredList2);
    }
}
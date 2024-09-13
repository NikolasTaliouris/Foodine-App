package com.capstone.foodineapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.capstone.foodineapp.Adapters.MyAdapter4;
import com.capstone.foodineapp.Models.User;
import com.capstone.foodineapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowUsers extends AppCompatActivity {


    RecyclerView recyclerView4;
    MyAdapter4 myAdapter4;
    ArrayList<User> userlist;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("User");

    //Initialize the recyclerview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);


        recyclerView4 = findViewById(R.id.userList);
        //It sets the size of the RecyclerView to be fixed,
        //It means that the layout manager can optimize the layout by not measuring each item during layout. Better Performance
        recyclerView4.setHasFixedSize(true);

        //It sets the layout manager for the RecyclerView.
        //A LinearLayoutManager is used to display items in a vertical scrolling list.

        recyclerView4.setLayoutManager(new LinearLayoutManager(this));
        userlist = new ArrayList<>();

        //It creates an instance of the adapter for the RecyclerView.
        //The adapter acts as a bridge between the RecyclerView and the data source.
        myAdapter4 = new MyAdapter4(this,userlist);

        //It sets the adapter for the RecyclerView,
        // The RecyclerView will use the adapter to display data.
        recyclerView4.setAdapter(myAdapter4);

        //Retrieve the Users from the database and add it inside of an Arraylist<User>

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    userlist.add(user);
                }
                myAdapter4.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
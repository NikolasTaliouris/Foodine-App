package com.capstone.foodineapp.Activities;

import static cz.msebera.android.httpclient.conn.ssl.SSLConnectionSocketFactory.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.capstone.foodineapp.Models.Food;
import com.capstone.foodineapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateItem extends AppCompatActivity {

    Spinner spinner;
    EditText createFoodName,createFoodDescription,createFoodPrice;
    Button btn_createItem;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mySpinner = database.getReference("Products");
    DatabaseReference database2 = database.getReference("Food");

    //In the onCreate method, the view elements are initialized by Id
    // and the click listener for the button is set,
    // The button creates a new Item
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        spinner = findViewById(R.id.spinner);
        createFoodName = findViewById(R.id.createFoodName);
        createFoodDescription = findViewById(R.id.createFoodDescription);
        createFoodPrice = findViewById(R.id.createFoodPrice);
        btn_createItem = findViewById(R.id.createItem);

        //We populate the spinner, in order for the admin to choose in which Category he wants to create the Item
        mySpinner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> productsList = new ArrayList<>();

                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    String productName = productSnapshot.child("productName").getValue(String.class);
                    productsList.add(productName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                        R.layout.spinneritem, productsList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //It creates the Item
        btn_createItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(createFoodName.getText().toString()).exists()){
                            Toast.makeText(CreateItem.this, "This item already exists!", Toast.LENGTH_SHORT).show();
                        }else {
                            Food food = new Food(createFoodName.getText().toString(),createFoodDescription.getText().toString(),createFoodPrice.getText().toString());
                            database2.child(spinner.getSelectedItem().toString()).child(database2.push().getKey()).setValue(food);
                            Toast.makeText(CreateItem.this, "Item has been registered", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }
}
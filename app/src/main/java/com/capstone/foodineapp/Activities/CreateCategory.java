package com.capstone.foodineapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.capstone.foodineapp.Models.Product;
import com.capstone.foodineapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateCategory extends AppCompatActivity {

    EditText createProductName, createProductDescription;
    Button btn_createCategory;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Products");

    //In the onCreate method, the view elements are initialized by Id
    // and the click listener for the button is set,
    // The button creates a new category
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        createProductName = findViewById(R.id.createProductName);
        createProductDescription = findViewById(R.id.createProductDescription);
        btn_createCategory = findViewById(R.id.createCategory);

        btn_createCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Checks if the category exists.
                //if it exists it displays an error message
                //if it doesn't it creates the category
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(createProductName.getText().toString()).exists()){
                            Toast.makeText(CreateCategory.this, "This Category already exists!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Product product = new Product(createProductName.getText().toString(),createProductDescription.getText().toString());
                            database.child(createProductName.getText().toString()).setValue(product);
                            Toast.makeText(CreateCategory.this, "Category has been Registered!", Toast.LENGTH_SHORT).show();
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
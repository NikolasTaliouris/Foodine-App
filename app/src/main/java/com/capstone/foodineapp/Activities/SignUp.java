package com.capstone.foodineapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.capstone.foodineapp.Models.User;
import com.capstone.foodineapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    EditText edtPhone,edtName,edtPassword;
    Button btnSignUp;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("User");


    //In the onCreate method, the view elements are initialized by Id
    // and the click listeners for the button is set,
    // The button create a user
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtPhone = findViewById(R.id.phone);
        edtName = findViewById(R.id.name);
        edtPassword = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.signup);

        //This button creates the user after checking if the user exists or not
        //If the user exists, it displays an error message
        //if the user doesn't exists, we store the user into the database and display a success message
        // IMPORTANT : WE CHECK IF THE USER EXISTS BASED ON THE PHONE THAT HE USES.
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(edtPhone.getText().toString()).exists()){
                            Toast.makeText(SignUp.this, "Phone number is already Registered!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            User user = new User(edtName.getText().toString(),edtPassword.getText().toString(),edtPhone.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this, "You have been signed up successfully!", Toast.LENGTH_SHORT).show();
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
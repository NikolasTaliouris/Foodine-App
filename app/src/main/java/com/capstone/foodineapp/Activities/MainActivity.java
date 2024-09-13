package com.capstone.foodineapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import com.capstone.foodineapp.R;

public class MainActivity extends AppCompatActivity {


    Button btnSignIn, btnSignUp;

    //In the onCreate method, the view elements are initialized by Id
    // and the click listeners for the buttons are set,
    // Each button starts a new activity by creating an intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.signIn);
        btnSignUp = findViewById(R.id.signUp);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIn = new Intent(MainActivity.this,SignIn.class);
                startActivity(signIn);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(MainActivity.this,SignUp.class);
                startActivity(signUp);
            }
        });
    }




}
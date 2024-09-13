package com.capstone.foodineapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.capstone.foodineapp.Models.User;
import com.capstone.foodineapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ModifyUser extends AppCompatActivity {


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("User");

    EditText changeName,changePassword;
    Button btn_updateUsername,btn_updatePassword;

    //In the onCreate method, the view elements are initialized by Id
    // and the click listeners for the buttons are set,
    // The buttons update the username and the password
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        User user = (User) getIntent().getSerializableExtra("user");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);

        changeName = findViewById(R.id.changeName);
        changePassword = findViewById(R.id.changePassword);
        btn_updateUsername = findViewById(R.id.updateUsername);
        btn_updatePassword = findViewById(R.id.updatePassword);


        // It takes the input from the EditText stores it in a variable and goes through the User database and updates the username
        //And it displays a message
        btn_updateUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = changeName.getText().toString();
                table_user.child(user.getPhone()).child("name").setValue(changeName.getText().toString());
                Toast.makeText(ModifyUser.this, "You changed your Username to : " + username , Toast.LENGTH_SHORT).show();
                Toast.makeText(ModifyUser.this, "Log out to update the Username", Toast.LENGTH_SHORT).show();
            }
        });

        // It takes the input from the EditText stores it in a variable and goes through the User database and updates the password
        //And it displays a message
        btn_updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table_user.child(user.getPhone()).child("name").setValue(changePassword.getText().toString());
                Toast.makeText(ModifyUser.this, "You just changed your password", Toast.LENGTH_SHORT).show();
                Toast.makeText(ModifyUser.this, "Log out to update the Password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
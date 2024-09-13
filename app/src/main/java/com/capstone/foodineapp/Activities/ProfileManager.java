package com.capstone.foodineapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.foodineapp.Models.User;
import com.capstone.foodineapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileManager extends AppCompatActivity {


    TextView phoneOfUser,nameOfUser,passwordOfUser;
    Button btn_deleteUser, btn_modifyUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference table_user = database.getReference("User");


    //In the onCreate method, the view elements are initialized by Id
    // and the click listeners for the buttons are set,
    // The buttons delete the current user or goes into the modifyUser activity for update the credentials
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_manager);

        User user = (User) getIntent().getSerializableExtra("user");
        phoneOfUser = findViewById(R.id.phoneofUser);
        nameOfUser = findViewById(R.id.nameofUser);
        passwordOfUser = findViewById(R.id.passwordofUser);
        btn_modifyUser = findViewById(R.id.modifyUser);
        btn_deleteUser = findViewById(R.id.deleteUser);


        //Checks if the user object is null or not
        if (user != null) {
            phoneOfUser.setText(user.getPhone());
            nameOfUser.setText(user.getName());
            passwordOfUser.setText(user.getPassword());
        }

        //Deletes the current user and it displays a message
        btn_deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        table_user.child(user.getPhone()).removeValue();
                        Toast.makeText(ProfileManager.this, "Your account has been deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileManager.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        //it sends your to the modifyUser activity to update the credentials
        btn_modifyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileManager.this,ModifyUser.class);
                intent.putExtra("user",user);
                startActivity(intent);
                finish();
            }
        });
    }
}
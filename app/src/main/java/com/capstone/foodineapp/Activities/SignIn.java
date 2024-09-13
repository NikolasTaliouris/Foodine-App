package com.capstone.foodineapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {

    EditText phone, password;
    Button btnLogIn;

    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("User");
    DatabaseReference database2 = FirebaseDatabase.getInstance().getReference("Cart");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // create a notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("1","notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // find views by ID
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        btnLogIn = findViewById(R.id.logIn);



        // remove any existing data in the Cart
        // get the user object from the database and
        // Checks if the user is an admin, start the Admin activity
        // if it's not the Admin -> the user is a regular user, start the Menu activity
        // when the button is clicked the notification will pop up to the Menu activity
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        database2.removeValue();
                        if (snapshot.child(phone.getText().toString()).exists()) {
                            User user = snapshot.child(phone.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(password.getText().toString())) {
                                if (phone.getText().toString().equals("6983260639") && password.getText().toString().equals("admin")) {
                                    Intent adminIntent = new Intent(SignIn.this, Admin.class);
                                    adminIntent.putExtra("user", user);
                                    startActivity(adminIntent);
                                    finish();
                                } else if (user.getPassword().equals(password.getText().toString())) {
                                    Intent menuIntent = new Intent(SignIn.this, Menu.class);
                                    menuIntent.putExtra("user", user);
                                    startActivity(menuIntent);
                                    showMostPopularItem();
                                    finish();
                                    Toast.makeText(SignIn.this, user.getName()+" just signed in", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignIn.this, "Sign in failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(SignIn.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }


    //It calculates the most popular food item based on the total quantity ordered from the Firebase Realtime Database
    //and sends a notification to the user with the name of the item. It's a Recommendation "System"
    public void showMostPopularItem() {
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Integer> itemQuantities = new HashMap<>();

                // calculate total quantity for each item
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot itemSnapshot : orderSnapshot.getChildren()) {
                        int quantity = itemSnapshot.child("quantity").getValue(Integer.class);
                        String foodName = itemSnapshot.child("food").child("foodName").getValue(String.class);
                        String foodDescription = itemSnapshot.child("food").child("description").getValue(String.class);
                        String foodPrice = itemSnapshot.child("food").child("price").getValue(String.class);

                        String key = foodName + " | " + foodDescription + " | " + foodPrice + "EUR";
                        int currentQuantity = itemQuantities.getOrDefault(key, 0);
                        itemQuantities.put(key, currentQuantity + quantity);
                    }
                }

                // find the most popular item
                String mostPopularItem = "";
                int maxQuantity = 0;
                for (String key : itemQuantities.keySet()) {
                    int quantity = itemQuantities.get(key);
                    if (quantity > maxQuantity) {
                        maxQuantity = quantity;
                        mostPopularItem = key;
                    }
                }

                /*It sets how the notification will look like*/

                final String CHANNEL_ID ="1";
                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),R.drawable.exlamention);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(SignIn.this,CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("FOODINE")
                        .setContentText("OFFER")
                        .setStyle(new NotificationCompat
                                .BigTextStyle().setBigContentTitle("New Offer")
                                .bigText("Do you want some : "+ mostPopularItem))
                        .setLargeIcon(largeIcon);

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.notify(1,builder.build());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handle error
            }
        });
    }
}
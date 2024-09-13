package com.capstone.foodineapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.capstone.foodineapp.Models.User;
import com.capstone.foodineapp.R;

public class Admin extends AppCompatActivity {

    TextView adminPage;
    Button btn_createCategory,btn_menu, btn_createItem,btn_showAllUsers;

    //In the onCreate method, the view elements are initialized by Id
    // and the click listeners for the buttons are set,
    // Each button starts a new activity by creating an intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        adminPage = findViewById(R.id.adminPage);
        btn_createCategory = findViewById(R.id.createCategory);
        btn_menu = findViewById(R.id.menu);
        btn_createItem = findViewById(R.id.createItem);
        btn_showAllUsers = findViewById(R.id.showAllUsers);

        btn_createCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin.this,CreateCategory.class);
                startActivity(intent);
            }
        });

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin.this,Menu.class);
                startActivity(intent);
            }
        });

        btn_createItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin.this,CreateItem.class);
                startActivity(intent);
            }
        });

        btn_showAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin.this,ShowUsers.class);
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
        User user = (User) getIntent().getSerializableExtra("user");
        switch (item.getItemId()){
            case R.id.Logout:
                finish();
                Intent intent = new Intent(Admin.this,SignIn.class);
                startActivity(intent);
                return true;
            case R.id.profile:
                Intent intent2 = new Intent(Admin.this,ProfileManager.class);
                intent2.putExtra("user",user);
                startActivity(intent2);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
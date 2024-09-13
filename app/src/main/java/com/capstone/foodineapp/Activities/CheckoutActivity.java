package com.capstone.foodineapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.foodineapp.Models.Cart;
import com.capstone.foodineapp.Models.Order;
import com.capstone.foodineapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CheckoutActivity extends AppCompatActivity {

    TextView orderID_label,total_amount;
    Button confirm_btn;
    private double totalPrice;
    ArrayList<Cart> cartlist;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Cart");
    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");

    //initialize the cart ArrayList.
    // Add a ValueEventListener to the cart database reference that calculates the total price of the order based on the items in the cart,
    // and sets the text of the total_amount TextView.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        cartlist = new ArrayList<>();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalPrice = 0;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Cart cart = postSnapshot.getValue(Cart.class);
                    cartlist.add(cart);
                    totalPrice += Double.parseDouble(cart.getFood().getPrice()) * cart.getQuantity();
                }
                total_amount = findViewById(R.id.totalAmount);
                total_amount.setText("Total amount: "+totalPrice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //get orderID from the query parameter
        Uri redirectUri = getIntent().getData();
        String orderID = redirectUri.getQueryParameter("token");

        //set orderID string to the UI
        orderID_label = findViewById(R.id.orderID);
        orderID_label.setText("Order ID: " +orderID);
        //Button to confirm the Order
        confirm_btn = findViewById(R.id.confirm_btn);

        //We confirm the order and we store the order in the database and we display a text
        confirm_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Order order = new Order();
                order.setOrderID(orderRef.push().getKey());
                orderRef.child(order.getOrderID()).setValue(cartlist);
                captureOrder(orderID); //function to finalize the payment
                Toast.makeText(CheckoutActivity.this, "Thank you for your order!", Toast.LENGTH_SHORT).show();
            }

            /*It takes the order ID as a parameter,
            and uses an AsyncHttpClient to make a POST request to the PayPal API to capture the payment.
            If the request is successful, the response is parsed and the user is redirected back to the menu page.
            If the request fails, an error message appears.*/
            void captureOrder(String orderID){
                //get the accessToken from MainActivity
                String accessToken = ShoppingCart.getAccessToken();
                AsyncHttpClient client = new AsyncHttpClient();
                client.addHeader("Accept", "application/json");
                client.addHeader("Content-type", "application/json");
                client.addHeader("Authorization", "Bearer " + accessToken);

                client.post("https://api-m.sandbox.paypal.com/v2/checkout/orders/"+orderID+"/capture", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        Log.e("RESPONSE", response);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        try {
                            JSONObject jobj = new JSONObject(response);
                            //redirect back to home page of app
                            Intent intent = new Intent(CheckoutActivity.this, Menu.class);
                            database.removeValue();
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
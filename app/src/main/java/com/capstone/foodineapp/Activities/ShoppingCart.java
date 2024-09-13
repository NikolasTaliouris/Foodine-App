package com.capstone.foodineapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.foodineapp.Adapters.MyAdapter3;
import com.capstone.foodineapp.Models.Cart;
import com.capstone.foodineapp.Models.Order;
import com.capstone.foodineapp.Models.User;
import com.capstone.foodineapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ShoppingCart extends AppCompatActivity {

    private static String accessToken;
    private String url;


    RecyclerView recyclerView3;


    Button btn_payorder,btn_paycash;
    MyAdapter3 myAdapter3;
    TextView totalPriceTextView;

    Button btn_refresh;


    public static String getAccessToken() {
        return accessToken;
    }

    ArrayList<Cart> cartlist;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Cart");
    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");


    //In the onCreate method, the view elements are initialized by Id
    // and the click listeners for the buttons are set,
    //Initialize the searchview and the recyclerview
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        recyclerView3 = findViewById(R.id.cartList);
        btn_paycash = findViewById(R.id.payCash);
        btn_payorder = findViewById(R.id.payOrder);
        totalPriceTextView = findViewById(R.id.total_price);
        btn_refresh = findViewById(R.id.btn_refresh);


        //It sets the size of the RecyclerView to be fixed,
        //It means that the layout manager can optimize the layout by not measuring each item during layout. Better Performance
        recyclerView3.setHasFixedSize(true);

        //It sets the layout manager for the RecyclerView.
        //A LinearLayoutManager is used to display items in a vertical scrolling list.
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));

        cartlist = new ArrayList<>();
        //It creates an instance of the adapter for the RecyclerView.
        //The adapter acts as a bridge between the RecyclerView and the data source.
        myAdapter3 = new MyAdapter3(this,cartlist);

        //It sets the adapter for the RecyclerView,
        // The RecyclerView will use the adapter to display data.
        recyclerView3.setAdapter(myAdapter3);


        // Initialize accessToken and url variables
        //IMPORTANT!!!: THE ACCESSTOKEN EXPIRES EVERY 8 HOURS, BECAUSE ITS A SANDBOX.
        //I AM GOING TO PROVIDE THE WAY HOW TO CREATE AN ACCESSTOKEN IN THE DOCUMENTATION
        accessToken = "A21AAIwsr2DnfFVWozds06jodYkLMsAZdqtCr_-4OIqdd_SXfyXD41J-CGZsWfTytXBfD6qU8kbAysOQATqIBN1hON1ousSrA";
        url = "https://api.sandbox.paypal.com";

        //Retrieve the Cart items from the database and add it inside of an Arraylist<Cart>

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Cart cart = postSnapshot.getValue(Cart.class);
                    cartlist.add(cart);
                }
                myAdapter3.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShoppingCart.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Refreshes the total price
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTotalPriceTextView();
            }
        });





        // It displays the total and sends the order,Also it stores the order to the Orders reference as history
        btn_paycash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTotalPrice();
                Toast.makeText(ShoppingCart.this, "Thanks for your Order!", Toast.LENGTH_SHORT).show();
                Order order = new Order();
                order.setOrderID(orderRef.push().getKey());
                orderRef.child(order.getOrderID()).setValue(cartlist);
                database.removeValue();
                finish();
            }
        });




        btn_payorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrder();
            }

            /* It creates an instance of AsyncHttpClient and sets the necessary headers for the HTTP request.
             It then creates a JSON-formatted order object as a string and sets it as the entity for the POST request.
             The post() method is called on the client object to send the HTTP request.
              The response from the server is handled using a TextHttpResponseHandler.
              If the request fails,logs displaying errors
              If the request is successful,It extracts the approval link from the response and launches it using a CustomTabsIntent.*/
            void createOrder(){
                AsyncHttpClient client = new AsyncHttpClient();
                client.addHeader("Accept", "application/json");
                client.addHeader("Content-type", "application/json");
                client.addHeader("Authorization", "Bearer " + accessToken);

                String order = "{"
                        + "\"intent\": \"CAPTURE\","
                        + "\"purchase_units\": [\n" +
                        "      {\n" +
                        "        \"amount\": {\n" +
                        "          \"currency_code\": \"EUR\",\n" +
                        "          \"value\": \"20.00\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    ],\"application_context\": {\n" +
                        "        \"brand_name\": \"TEST_MYAPP\",\n" +
                        "        \"return_url\": \"myapp://checkout\",\n" +
                        "        \"cancel_url\": \"https://example.com\"\n" +
                        "    }}";

                HttpEntity entity = new StringEntity(order, "utf-8");

                client.post(getApplicationContext(), url + "/v2/checkout/orders",entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("RESPONSE",responseString);

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            JSONArray links = new JSONObject(responseString).getJSONArray("links");

                            //iterate  array to get the approval link
                            for (int i = 0; i < links.length(); ++i) {
                                String rel = links.getJSONObject(i).getString("rel");
                                if (rel.equals("approve")){
                                    String link = links.getJSONObject(i).getString("href");

                                    //go to this link through CCT
                                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                    CustomTabsIntent customTabsIntent = builder.build();
                                    customTabsIntent.launchUrl(ShoppingCart.this, Uri.parse(link));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    //It iterates thought the cartlist and  updates the total price,
    // it updates the textview.
    private void updateTotalPriceTextView(){
        double totalPrice = 0.0;
        for (Cart cart : cartlist) {
            double itemPrice = Double.parseDouble(cart.getFood().getPrice());
            int itemQuantity = cart.getQuantity();
            totalPrice += itemPrice * itemQuantity;
        }
        totalPriceTextView.setText("Total Cost: "+ totalPrice);
    }

    //it iterates through the cartItems to calculate the total
    private double calculateTotalPrice(List<Cart> cartItems) {
        double totalPrice = 0.0;
        if (cartItems != null) {
            for (Cart cart : cartItems) {
                double itemPrice = Double.parseDouble(cart.getFood().getPrice());
                int itemQuantity = cart.getQuantity();
                totalPrice += itemPrice * itemQuantity;
            }
        }
        return totalPrice;
    }


    //it updates the total price in the shopping Cart automatically,
    // every time something is changing and it displays a text with the total price

    private double updateTotalPrice() {
        double totalPrice = calculateTotalPrice(cartlist);
        Toast.makeText(this, "Your total cost is :" + totalPrice + " EUR", Toast.LENGTH_SHORT).show();
        return totalPrice;
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
                Intent intent = new Intent(ShoppingCart.this,SignIn.class);
                startActivity(intent);
                return true;
            case R.id.profile:
                Intent intent2 = new Intent(ShoppingCart.this,ProfileManager.class);
                intent2.putExtra("user",user);
                startActivity(intent2);
        }
        return super.onOptionsItemSelected(item);
    }

}
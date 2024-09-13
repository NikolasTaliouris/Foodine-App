package com.capstone.foodineapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.foodineapp.Models.User;
import com.capstone.foodineapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*The code is implementing an adapter for a RecyclerView that displays a list of items.*/
public class MyAdapter4 extends RecyclerView.Adapter<MyAdapter4.MyViewHolder4> {

    Context context4;
    ArrayList<User> userlist1;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myUser = database.getReference("User");

    //The Adapter constructor -> a Context and an ArrayList of user objects.
    // The Context is used to inflate the layout for each item in the onCreateViewHolder() method.
    public MyAdapter4(Context context4, ArrayList<User> userlist) {
        this.context4 = context4;
        this.userlist1 = userlist;
    }

    @NonNull
    @Override
    public MyAdapter4.MyViewHolder4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context4).inflate(R.layout.useritem,parent,false);
        return new MyAdapter4.MyViewHolder4(view);
    }

    //The onBindViewHolder() method is called for each item in the RecyclerView,
    // and it binds the data from User object at the current position to the views in the MyViewHolder.
    @Override
    public void onBindViewHolder(@NonNull MyAdapter4.MyViewHolder4 holder, int position) {

        int userPosition = holder.getAbsoluteAdapterPosition();
        User user = userlist1.get(position);
        holder.phoneNumber.setText(user.getPhone());
        holder.userName.setText(user.getName());
        holder.passWord.setText(user.getPassword());

        //The remove button, Removes the user from the User list in Admin,
        //It updates the adapter and it removes the item from the database
        holder.btn_removeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (userPosition>=0 && userPosition < userlist1.size()){
                            userlist1.remove(userPosition);
                            myUser.child(user.getPhone()).removeValue();
                            notifyDataSetChanged();
                            Toast.makeText(context4, "User removed successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    //returns the number of items in the ArrayList.
    @Override
    public int getItemCount() {
        return userlist1.size();
    }

    // It defines the views for each item in the RecyclerView.
    // It has three TextViews for the phoneNumber,userName,passWord.
    //And a button btn_removeUser to remove the user
    public static class MyViewHolder4 extends RecyclerView.ViewHolder{
        TextView phoneNumber,userName,passWord;
        Button btn_removeUser;
        public MyViewHolder4(@NonNull View itemView) {
            super(itemView);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            userName = itemView.findViewById(R.id.userName);
            passWord = itemView.findViewById(R.id.passWord);
            btn_removeUser = itemView.findViewById(R.id.removeUser);
        }
    }
}

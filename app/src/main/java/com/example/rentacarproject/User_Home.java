package com.example.rentacarproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class User_Home extends AppCompatActivity {

    private User_Form u;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference ref_users;
    private DatabaseReference ref_owner;
    private ListView listView;
    private ArrayList<Owner_Form> rest_f = new ArrayList<>();
    private ArrayList<String> owner_list = new ArrayList<>();
    private ArrayAdapter<String> owner_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);


        listView = (ListView) findViewById(R.id.owner_view);
        ref_users = FirebaseDatabase.getInstance().getReference("User");
        ref_owner = FirebaseDatabase.getInstance().getReference("Owner");


        ref_users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.child(user.getUid()).getValue(User_Form.class);
                TextView Hello_Name = findViewById(R.id.hello_name);
                Hello_Name.setText("Hello, " + u.getFirstName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent i_page = new Intent(User_Home.this, makeOrder.class);

                i_page.putExtra("owner_uid", rest_f.get(i).getUID());
                startActivity(i_page);
            }
        });
        getData();
    }






    public void getData() {

       try {
           Query query = ref_owner.orderByChild("location");
           query.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()) {
                       String owner_string;
                       for (DataSnapshot db : dataSnapshot.getChildren()) {
                           owner_string = db.child("name").getValue(String.class);
                           Owner_Form temp_rest = db.getValue(Owner_Form.class);
                           rest_f.add(temp_rest);
                           owner_list.add(owner_string);

                       }
                       owner_adapter = new ArrayAdapter<String>(User_Home.this, R.layout.customefont, owner_list);
                       listView.setAdapter(owner_adapter);


                   } else {
                       owner_list.add("No Owner here");
                       owner_adapter = new ArrayAdapter<String>(User_Home.this, R.layout.customefont, owner_list);
                       listView.setAdapter(owner_adapter);
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {
               }


           });
       }catch (Exception e){}
    }

}
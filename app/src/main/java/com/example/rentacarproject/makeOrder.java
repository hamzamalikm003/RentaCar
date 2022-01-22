package com.example.rentacarproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class makeOrder extends AppCompatActivity implements View.OnClickListener {


    private ListView listview;
    private String owner_uid;
    private ArrayList<Car_Add> car_menu = new ArrayList<>();
    private ArrayAdapter<Car_Add> car_addapter;
    private DatabaseReference menu_db = FirebaseDatabase.getInstance().getReference("Menus");
    private String user_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private OrderForm order;
    private TextView totalptv;
    private Button placeorderbtn;
    private DatabaseReference users = FirebaseDatabase.getInstance().getReference("User").child(user_uid);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);


        placeorderbtn = findViewById(R.id.placeOrd_btn);
        listview = findViewById(R.id.Listview_Make_Order);
        totalptv = findViewById(R.id.totalOrd_TV);
        Intent i = getIntent();
        owner_uid = (String) i.getSerializableExtra("owner_uid");
        final String ordernum = owner_uid + user_uid + (((int) ((Math.random()) * 10000)));
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    order = new OrderForm(ordernum, owner_uid, user_uid, "unhandled");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    order.addCar(car_menu.get(i));
                    totalptv.setText("Total Order: " + order.getTotal_price());

            }
        });
        placeorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ordernum = order.getOrder_num();
                String ownerid = order.getOwner_id();
                String clientid = order.getClient_id();
                String status = order.getStatus();

                Intent i = new Intent(makeOrder.this, Place_Order.class);
                Bundle extras = new Bundle();
                extras.putString("order_id", ordernum);
                extras.putString("owner_id", ownerid);
                extras.putString("client_id", clientid);
                extras.putString("status", status);
                extras.putDouble("price", order.getTotal_price());

                ArrayList<String> cars = new ArrayList<>();

                for (int j = 0; j < order.getCars_orderd().size(); j++) {
                    cars.add(order.getCars_orderd().get(j).toString());
                }

                extras.putStringArrayList("cars", cars);
                i.putExtra("extras", extras);
                startActivity(i);

            }
        });
        getdata();
    }

    private void getdata() {
        try {
            menu_db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists() && dataSnapshot.child(owner_uid).exists()) {
                        double price;
                        String name_car;
                        String desc_car;

                        DataSnapshot snep = dataSnapshot.child(owner_uid);
                        if (snep.child("car_list").exists()) {
                            for (DataSnapshot db : snep.child("car_list").getChildren()) {
                                price = db.child("price").getValue(double.class);
                                name_car = db.child("car_name").getValue(String.class);
                                desc_car = db.child("car_description").getValue(String.class);
                                Car_Add temp = new Car_Add(price, name_car, desc_car);
                                car_menu.add(temp);

                            }
                        }
                        if (snep.child("truck_list").exists()) {
                            for (DataSnapshot db : snep.child("truck_list").getChildren()) {
                                price = db.child("price").getValue(double.class);
                                name_car = db.child("car_name").getValue(String.class);
                                desc_car = db.child("car_description").getValue(String.class);
                                Car_Add temp = new Car_Add(price, name_car, desc_car);
                                car_menu.add(temp);

                            }
                        }
                        if (snep.child("bus_list").exists()) {
                            for (DataSnapshot db : snep.child("bus_list").getChildren()) {
                                price = db.child("price").getValue(double.class);
                                name_car = db.child("car_name").getValue(String.class);
                                desc_car = db.child("car_description").getValue(String.class);
                                Car_Add temp = new Car_Add(price, name_car, desc_car);
                                car_menu.add(temp);

                            }
                        }
                        car_addapter = new ArrayAdapter<Car_Add>(makeOrder.this, R.layout.customefont, car_menu);



                        listview.setAdapter(car_addapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){}

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
        getdata();
    }


}
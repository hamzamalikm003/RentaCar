package com.example.rentacarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Owner_Home extends AppCompatActivity {

    private Button addCar;
    private DatabaseReference ref_menus;
    private String UID;
    private ListView activeOrders_listView;
    private ArrayList<OrderForm> activeOrders_list = new ArrayList<>();
    private ArrayAdapter<OrderForm> activeOrders_adapter;
    private ListView menu;
    private ArrayList<DataSnapshot> all_needed_data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home);
        addCar=findViewById(R.id.addCar);
        ref_menus = FirebaseDatabase.getInstance().getReference("Menus");
        menu = findViewById(R.id.activeOrders_listView);
        activeOrders_listView = findViewById(R.id.activeOrders_listView);
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Owner_Home.this, Menu.class);
                startActivity(i);
            }
        });
        FirebaseDatabase.getInstance().getReference("Orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!activeOrders_list.isEmpty()) {
                            activeOrders_list.clear();
                            activeOrders_adapter.clear();
                            activeOrders_listView.clearAnimation();
                        }
                        if (dataSnapshot.exists()) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String owner_id = snapshot.child("owner_id").getValue(String.class);
                                String status = snapshot.child("status").getValue(String.class);
                                String client_id = snapshot.child("client_id").getValue(String.class);
                                if (!owner_id.equals(UID) || !(status.equals("unhandled") || status.equals("seen")
                                        || status.equals("preparation") || status.equals("on the way")
                                        || status.equals("received")))
                                    continue;
                                if (status.equals("unhandled"))
                                    all_needed_data.add(snapshot);
                                String order_num = snapshot.child("order_num").getValue(String.class);

                                OrderForm curr_order = new OrderForm(order_num, owner_id, client_id, status);
                                for (DataSnapshot snapshot_car : snapshot.child("cars_orderd").getChildren()) {
                                    double price = snapshot_car.child("price").getValue(double.class);
                                    String car_name = snapshot_car.child("car_name").getValue(String.class);
                                    String car_desc = snapshot_car.child("car_description").getValue(String.class);
                                    Car_Add curr_car = new Car_Add(price, car_name, car_desc);
                                    curr_order.addCar(curr_car);

                                }
                                activeOrders_list.add(curr_order);
                            }
                            activeOrders_adapter = new ArrayAdapter<OrderForm>(Owner_Home.this, android.R.layout.simple_list_item_1, activeOrders_list);
                            activeOrders_listView.setAdapter(activeOrders_adapter);
                        } else {
                            Toast.makeText(Owner_Home.this, "no orders for this car yet ", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        activeOrders_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (activeOrders_list.get(i).getStatus()) {
                    case "unhandled":
                        update_status_DB(activeOrders_list.get(i).getOrder_num(), "seen");
                        activeOrders_list.get(i).setStatus("seen");
                        break;
                    case "seen":
                        update_status_DB(activeOrders_list.get(i).getOrder_num(), "preparation");
                        activeOrders_list.get(i).setStatus("preparation");
                        break;
                    case "preparation":
                        update_status_DB(activeOrders_list.get(i).getOrder_num(), "on the way");
                        activeOrders_list.get(i).setStatus("on the way");
                        break;
                    case "on the way":
                        update_status_DB(activeOrders_list.get(i).getOrder_num(), "received");
                        activeOrders_list.get(i).setStatus("received");
                        break;
                    case "received":
                        update_status_DB(activeOrders_list.get(i).getOrder_num(), "done");
                        activeOrders_list.get(i).setStatus("done");
                        break;
                    case "done":
                        activeOrders_list.remove(i);
                        break;
                }
                activeOrders_adapter = new ArrayAdapter<OrderForm>(Owner_Home.this, android.R.layout.simple_list_item_1, activeOrders_list);
                activeOrders_listView.setAdapter(activeOrders_adapter);
            }
        });


    }

    private void update_status_DB(String order_num, String new_status) {
        FirebaseDatabase.getInstance().getReference("Orders").child(order_num).child("status").setValue(new_status);
    }
}
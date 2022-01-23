package com.example.rentacarproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Random;

public class PageOrder extends AppCompatActivity implements View.OnClickListener{

    private ListView listview;
    private ArrayList<OrderForm> orders_list;
    private ArrayAdapter<OrderForm> addapter;
    private final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private final DatabaseReference ref_orders = FirebaseDatabase.getInstance().getReference("Orders");
    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_order);


        orders_list = new ArrayList<>();
        flag = false;
        listview = findViewById(R.id.ListView_order_page);
        ref_orders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!orders_list.isEmpty()) {
                    orders_list.clear();
                    addapter.clear();
                    listview.clearAnimation();
                }
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snep : dataSnapshot.getChildren()) {

                        if (snep.child("client_id").getValue().equals(uid) && !snep.child("status").getValue().equals("done")) {
                            String order_num = snep.child("order_num").getValue(String.class);
                            String owner_id = snep.child("owner_id").getValue(String.class);
                            String client_id = snep.child("client_id").getValue(String.class);
                            String status = snep.child("status").getValue(String.class);
                            double total_price = snep.child("total_price").getValue(double.class);

                            OrderForm order = new OrderForm(order_num, owner_id, client_id, status);
                            DataSnapshot cars_orderd = snep.child("cars_orderd");
                            for (DataSnapshot child_dt : cars_orderd.getChildren()) {
                                double price = child_dt.child("price").getValue(double.class);
                                String car_name = child_dt.child("car_name").getValue(String.class);
                                String car_description = child_dt.child("car_description").getValue(String.class);
                                Car_Add dish = new Car_Add(price, car_name, car_description);
                                order.addCar(dish);
                            }
                            orders_list.add(order);
                        }
                    }
                    if (flag == true) {
                        notify_order_status();
                    }
                    flag = true;
                }
                addapter = new ArrayAdapter<OrderForm>(PageOrder.this, android.R.layout.simple_list_item_1, orders_list);
                listview.setAdapter(addapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void notify_order_status() {
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        NotificationManager notif_manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelld = "My_Channelld";
            NotificationChannel channel = new NotificationChannel(channelld, "channel title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("description");
            notif_manager.createNotificationChannel(channel);
            NotificationCompat.Builder builder = new
                    NotificationCompat.Builder(getApplicationContext(), channelld);
            builder.setChannelId(channelld);
            Notification notification = builder
                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_focused)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle("Status Change")
                    .setContentText("your order status has been updated").build();
            notif_manager.notify(m,notification);
        }
    }


    //click function on personal settings button
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Orders_history_btn:
                Intent i = new Intent(PageOrder.this, OrderHistoryUser.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



}
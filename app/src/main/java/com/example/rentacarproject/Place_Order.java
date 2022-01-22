package com.example.rentacarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Place_Order extends AppCompatActivity {


    private TextView ordernum_view;
    private TextView totalprice_view;
    private Button orderbtn;
    private ListView listview;
    private ArrayList<String> cars_str = new ArrayList<>();
    private ArrayList<Car_Add> cars = new ArrayList<>();
    private ArrayAdapter<Car_Add> addapter;
    private double total_price;
    private OrderForm order;
    private final DatabaseReference ordersdata = FirebaseDatabase.getInstance().getReference("Orders");
    private String City;
    private String Street;
    private String House_num;
    private String Phone_num;
    private boolean bool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);





        ordernum_view = findViewById(R.id.orderNum_TV);
        totalprice_view = findViewById(R.id.ordPrice_TV);
        orderbtn = findViewById(R.id.Ord_btn);
        listview = findViewById(R.id.ListView_place_order);
        Intent i = getIntent();
        Bundle extras = i.getBundleExtra("extras");
        final String order_num = extras.getString("order_id");
        String owner_id = extras.getString("owner_id");
        String client_id = extras.getString("client_id");
        String status = extras.getString("status");
        total_price = extras.getDouble("price");
        City = extras.getString("City");
        Street = extras.getString("Street");
        House_num = extras.getString("House_num");
        Phone_num = extras.getString("Phone_num");

        cars_str.addAll(extras.getStringArrayList("cars"));
        order = new OrderForm(order_num, owner_id, client_id, status);
        string_to_cars_array();
        addapter = new ArrayAdapter<>(Place_Order.this, R.layout.customefont, cars);
        totalprice_view.setText("price: " + Double.toString(total_price));
        ordernum_view.setText("order number" + order_num.replaceAll("[^0-9]", ""));
        listview.setAdapter(addapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                order.removeCar(cars.get(i));
                cars.remove(i);
                addapter = new ArrayAdapter<>(Place_Order.this, R.layout.customefont, cars);
                listview.setAdapter(addapter);
                total_price = order.getTotal_price();
                totalprice_view.setText("price: " + total_price);
            }
        });

        orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordersdata.child(order_num).setValue(order);
                Intent i=new Intent(Place_Order.this,User_Home.class);
                startActivity(i);

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    private void string_to_cars_array() {
        for (int j = 0; j < cars_str.size(); j++) {
            String[] split = cars_str.get(j).split(" ");
            String name = split[0];
            double price = Double.parseDouble(split[3]);;
            String desc = split[7];
            Car_Add car1 = new Car_Add(price, name, desc);
            cars.add(car1);
        }
        for (int i = 0; i < cars.size(); i++) {
            order.addCar(cars.get(i));
        }
    }

}
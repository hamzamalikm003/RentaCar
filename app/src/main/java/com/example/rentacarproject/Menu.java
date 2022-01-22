package com.example.rentacarproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Menu extends AppCompatActivity implements View.OnClickListener {



    private CheckBox car;
    private CheckBox truck;
    private CheckBox bus;
    private DatabaseReference ref_menus;
    private TextView menu_name;
    private Menu_Form data_menu;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<String> arr;
    Menu_Adapter addapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);



        data_menu = new Menu_Form();
        ref_menus = FirebaseDatabase.getInstance().getReference("Menus");
        list = findViewById(R.id.Menu_List);
        arr = new ArrayList<String>();
        findViewById(R.id.Add_Car).setOnClickListener(this);
        get_menu("Car");
        get_menu("Truck");
        get_menu("Bus");



    }
    private void get_menu(String str){
        final String type_car = str;
        ref_menus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.child(user.getUid()).exists()) { //if the user has menu
                    DataSnapshot user_menu = dataSnapshot.child(user.getUid());
                    if (type_car == "Car") { //if we chose starters then add it to menu and add it to the list we display (arr)
                        add_carslist_menu_todata_menu(user_menu);
                        get_arrtype(arr,data_menu.getCar_list());


                    } else if (type_car == "Truck") { //same
                        add_trucklist_menu_todata_menu(user_menu);
                        get_arrtype(arr,data_menu.getTruck_list() );


                    } else if (type_car == "Bus") { //same
                        add_buslist_menu_todata_menu(user_menu);
                        get_arrtype(arr,data_menu.getBus_list() );


                    }
                    addapter = new Menu_Adapter(arr,Menu.this,type_car,data_menu); // intilize addapter
                    list.setAdapter(addapter); //show the addapter
                    data_menu.clear_buses();
                    data_menu.clear_cars();
                    data_menu.clear_trucks();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void add_carslist_menu_todata_menu(DataSnapshot dt) {
        DataSnapshot maindata = dt.child("car_list");
        for (DataSnapshot childdata : maindata.getChildren()) {
            String name = childdata.child("car_name").getValue(String.class);
            String desc = childdata.child("car_description").getValue(String.class);
            double price = childdata.child("price").getValue(double.class);
            Car_Add car1 = new Car_Add(price, name, desc);
            data_menu.add_car(car1);
        }
    }
    private void add_trucklist_menu_todata_menu(DataSnapshot dt) {
        DataSnapshot maindata = dt.child("truck_list");
        for (DataSnapshot childdata : maindata.getChildren()) {
            String name = childdata.child("car_name").getValue(String.class);
            String desc = childdata.child("car_description").getValue(String.class);
            double price = childdata.child("price").getValue(double.class);
            Car_Add car1 = new Car_Add(price, name, desc);
            data_menu.add_truck(car1);
        }
    }
    private void add_buslist_menu_todata_menu(DataSnapshot dt) {
        DataSnapshot maindata = dt.child("bus_list");
        for (DataSnapshot childdata : maindata.getChildren()) {
            String name = childdata.child("car_name").getValue(String.class);
            String desc = childdata.child("car_description").getValue(String.class);
            double price = childdata.child("price").getValue(double.class);
            Car_Add car1 = new Car_Add(price, name, desc);
            data_menu.add_bus(car1);
        }
    }



    @Override
    public void onClick(View v) { //make sure only one chekbox will be chek
        switch (v.getId()) {

            case R.id.Add_Car:
                Intent i = new Intent(this, AddCar.class);
                startActivity(i);

        }
    }
    public void get_arrtype(ArrayList<String> arr_list, ArrayList<Car_Add> cares){
        for (int i = 0; i < cares.size(); i++){
            arr_list.add(cares.get(i).toString());
        }
    }
}

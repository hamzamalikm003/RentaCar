package com.example.rentacarproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCar extends AppCompatActivity implements View.OnClickListener{

    private TextView name;
    private TextView price;
    private TextView description;
    private Button add;
    private CheckBox car, truck , bus ;
    private DatabaseReference menus;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private boolean menu_exist;
    private boolean menu_empty;
    private Menu_Form arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        name = (TextView) findViewById(R.id.car_name);
        price = (TextView) findViewById(R.id.car_price);
        description = (TextView) findViewById(R.id.car_desc);
        add = (Button) findViewById(R.id.add_button);

        car = (CheckBox) findViewById(R.id.car);
        truck = (CheckBox) findViewById(R.id.truck);
        bus = (CheckBox) findViewById(R.id.bus);
        menu_empty = false;
        menu_exist = false;
        menus = FirebaseDatabase.getInstance().getReference("Menus");
        arr = new Menu_Form();
        get_menu();

        add.setOnClickListener(new View.OnClickListener() { //add button function
            @Override
            public void onClick(View view) {
                String namestr = name.getText().toString().trim();
                String desc = description.getText().toString().trim();
                String pricestr = price.getText().toString().trim();
                double price_doub;
                if (pricestr.isEmpty()) { //chek if there is a price
                    price.setError("price filed is empty");
                    price.requestFocus();
                    return;
                } else {
                    price_doub = Double.parseDouble(pricestr);
                }
                if (namestr.isEmpty()) { //chek if there is a name to the dish
                    name.setError(" name is empty");
                    name.requestFocus();
                    return;
                }
                Car_Add car1 = new Car_Add(price_doub, namestr, desc);
                if (menu_exist == true) {
                    AddCar_toexist_menu(car1);

                } else if (menu_empty == true) {
                    write_menu_indata(car1);
                }
            }
        });

    }

    private void write_menu_indata(Car_Add car1) { //write the first dish and create a menu in data base
        boolean flag = true; //chek if one of the box is cheked
        if (car.isChecked()) {
            arr.add_car(car1);
        } else if (truck.isChecked()) {
            arr.add_truck(car1);

        } else if (bus.isChecked()) {
            arr.add_bus(car1);

        } else { //if the client dident chek any box
            bus.setError("please choose car type");
            bus.requestFocus();
            truck.requestFocus();
            car.requestFocus();

            flag = false;

        }
        if (flag == true) { //if one of the boxs is check add to arr
            menus.child(user.getUid()).setValue(arr);
            menu_empty = false; //now there is a menu in database
            menu_exist = true;
        }
    }

    private void AddCar_toexist_menu(Car_Add car1) {
        if (arr.exist(car1)) {
            Toast.makeText(AddCar.this, "this car is alredy on the menu", Toast.LENGTH_SHORT).show();
        }  else if (car.isChecked()) {
            arr.add_car(car1);
            menus.child(user.getUid()).child("car_list").push().setValue(car1);
            Toast.makeText(AddCar.this, "car added", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(AddCar.this, Owner_Home.class);
            startActivity(i);

        } else if (truck.isChecked()) {
            arr.add_truck(car1);
            menus.child(user.getUid()).child("truck_list").push().setValue(car1);
            Toast.makeText(AddCar.this, "car added", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(AddCar.this, Owner_Home.class);
            startActivity(i);


        } else if (bus.isChecked()) { //add to other menu
            arr.add_bus(car1);
            menus.child(user.getUid()).child("bus_list").push().setValue(car1);
            Toast.makeText(AddCar.this, "car added", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(AddCar.this, Owner_Home.class);
            startActivity(i);

        } else {
            Toast.makeText(AddCar.this, "please select car type", Toast.LENGTH_SHORT).show();

            bus.requestFocus();
            truck.requestFocus();
            car.requestFocus();

        }
    }




    public void get_menu() {
        menus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.child(user.getUid()).exists()) {
                    DataSnapshot user_menu = dataSnapshot.child(user.getUid());
                    if (user_menu.child("car_list").exists()) {
                        add_carlist_menu_toarr(user_menu);
                    }

                    if (user_menu.child("truck_list").exists()) {
                        add_trucklist_menu_toarr(user_menu);
                    }

                    if (user_menu.child("bus_list").exists()) {
                        add_buslist_menu_toarr(user_menu);
                    }

                    menu_exist = true;

                } else { //if there is no menu then create an emty one
                    arr = new Menu_Form();
                    menu_empty = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();

            }
        });
    }

    private void add_buslist_menu_toarr(DataSnapshot dt) {
        DataSnapshot fooddata = dt.child("bus_list");
        for (DataSnapshot childdata : fooddata.getChildren()) {
            String name = childdata.child("car_name").getValue(String.class);
            String desc = childdata.child("car_discription").getValue(String.class);
            double price = childdata.child("price").getValue(double.class);
            Car_Add car1 = new Car_Add(price, name, desc);
            arr.add_bus(car1);

        }
    }

    private void add_trucklist_menu_toarr(DataSnapshot dt) {
        DataSnapshot fooddata = dt.child("truck_list");
        for (DataSnapshot childdata : fooddata.getChildren()) {
            String name = childdata.child("car_name").getValue(String.class);
            String desc = childdata.child("car_discription").getValue(String.class);
            double price = childdata.child("price").getValue(double.class);
            Car_Add car1 = new Car_Add(price, name, desc);
            arr.add_truck(car1);

        }
    }



    private void add_carlist_menu_toarr(DataSnapshot dt) {
        DataSnapshot fooddata = dt.child("car_list");
        for (DataSnapshot childdata : fooddata.getChildren()) {
            String name = childdata.child("car_name").getValue(String.class);
            String desc = childdata.child("car_discription").getValue(String.class);
            double price = childdata.child("price").getValue(double.class);
            Car_Add car1 = new Car_Add(price, name, desc);
            arr.add_car(car1);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public void onClick(View v) { //make sure only one chekbox will be chek
        switch (v.getId()) {

            case R.id.bus:
                if (car.isChecked()) {
                    car.setChecked(false);
                }

                if (truck.isChecked()) {
                    truck.setChecked(false);
                }
                break;
            case R.id.truck:
                if (car.isChecked()) {
                    car.setChecked(false);
                }
                if (bus.isChecked()) {
                    bus.setChecked(false);
                }

                break;
            case R.id.car:

                if (bus.isChecked()) {
                    bus.setChecked(false);
                }
                if (truck.isChecked()) {
                    truck.setChecked(false);
                }
                break;

        }
    }
}
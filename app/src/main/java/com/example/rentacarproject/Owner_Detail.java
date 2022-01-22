package com.example.rentacarproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Owner_Detail extends AppCompatActivity implements View.OnClickListener {

    private String UID;
    private EditText OwnerName;
    private EditText Phone;
    private EditText city;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_detail);
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        OwnerName=findViewById(R.id.owner_name);
        Phone=findViewById(R.id.owner_phone);
        city=findViewById(R.id.owner_city);
        description=findViewById(R.id.description_text);
        findViewById(R.id.finish_btn).setOnClickListener(this);



    }
    private void register_owner() {
       try {
           final String rest_name = OwnerName.getText().toString().trim();
           final String location = city.getText().toString().trim();
           final String phone = Phone.getText().toString().trim();
           final String descrip = description.getText().toString().trim();



           if (rest_name.isEmpty()) {
               OwnerName.setError("Restaurant Name is required");
               OwnerName.requestFocus();
               return;
           }
           if (phone.isEmpty()) {
               Phone.setError("Phone is empty");
               Phone.requestFocus();
               return;
           }
           if (location.isEmpty()) {
               city.setError("city is empty");
               city.requestFocus();
               return;
           }
           if (descrip.isEmpty()) {
               description.setError("description is empty");
               description.requestFocus();
               return;
           }


           Owner_Form owner1 = new Owner_Form(rest_name, location, phone,descrip,UID);
           FirebaseDatabase.getInstance().getReference("Owner").child(UID).setValue(owner1)
                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()) {
                               Intent i = new Intent(Owner_Detail.this, Owner_Home.class);
                               startActivity(i);

                           } else {
                               Toast.makeText(Owner_Detail.this, "Sign up failed", Toast.LENGTH_SHORT).show();

                           }
                       }
                   });


       }catch (Exception e)
       {}
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish_btn:
                register_owner();
                break;
        }
    }
}
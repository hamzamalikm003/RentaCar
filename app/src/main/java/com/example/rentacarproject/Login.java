package com.example.rentacarproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth authFB;
    private EditText email_ID, password_ID;
    private DatabaseReference ref_users;
    private String UID;
    private String owner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        TextView signUp = findViewById(R.id.signUp_text);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, SignUp.class);
                startActivity(i);
            }
        });


        findViewById(R.id.loginBotton).setOnClickListener(this);
        authFB = FirebaseAuth.getInstance();
        email_ID = findViewById(R.id.EmailLog);
        password_ID = findViewById(R.id.passwordLog);


    }

    public void LOGIN() {
       try {
           String email = email_ID.getText().toString();
           String password = password_ID.getText().toString();
           if (email.isEmpty()) {
               email_ID.setError("Missing Email");
               email_ID.requestFocus();

           } else if (password.isEmpty()) {
               password_ID.setError("Missing Password");
               password_ID.requestFocus();

           } else if (!(email.isEmpty() && password.isEmpty())) {
               authFB.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (!task.isSuccessful()) {
                           Toast.makeText(Login.this, "Login Error", Toast.LENGTH_LONG).show();
                       } else {
                           Toast.makeText(Login.this, "Log In Successful", Toast.LENGTH_LONG).show();

                           ref_users = FirebaseDatabase.getInstance().getReference("User");
                           UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                           ref_users.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   owner = dataSnapshot.child(UID).child("owner").getValue().toString();

                                   if (owner.equals("admin")) {
                                       Intent k = new Intent(Login.this, Login.class);
                                       startActivity(k);
                                       finish();
                                   }

                                   else if (owner.equals("no")) {
                                       Intent i = new Intent(Login.this, User_Home.class);
                                       startActivity(i);
                                       finish();
                                   }

                                   else {
                                       Intent j = new Intent(Login.this, Owner_Home.class);
                                       startActivity(j);
                                       finish();
                                   }
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {

                               }
                           });

                       }
                   }
               });
           } else {
               Toast.makeText(Login.this, "Error, Try again", Toast.LENGTH_LONG).show();
           }


       }catch (Exception e)
       {}
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBotton:
                LOGIN();
                break;
        }
    }

}
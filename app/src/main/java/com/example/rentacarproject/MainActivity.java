package com.example.rentacarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button b1;
    private Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=findViewById(R.id.logInButton);
        b2=findViewById(R.id.signUpButton);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logInButton:
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                break;
            case R.id.signUpButton:
                Intent j = new Intent(MainActivity.this, SignUp.class);
                startActivity(j);
                break;
        }
    }
}
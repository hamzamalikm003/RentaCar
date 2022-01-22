package com.example.rentacarproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseAuth authFB;
    private Button signUpButton;
    private TextView goLogin;

    private Switch aSwitch;
    private Boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextFirstName = findViewById(R.id.firstNameText);
        editTextLastName = findViewById(R.id.lastNameText);
        editTextEmail = findViewById(R.id.emailText);
        editTextPassword = findViewById(R.id.passwordText);
        signUpButton=findViewById(R.id.signUpButton);
        goLogin=findViewById(R.id.goToLogInText);
        goLogin.setOnClickListener( this);
        signUpButton.setOnClickListener(this);
        authFB = FirebaseAuth.getInstance();

        aSwitch = (Switch) findViewById(R.id.switch2);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authFB.getCurrentUser() != null) {
            //handle the already login user
        }
    }

    private void registerUser() {
        final String FirstName = editTextFirstName.getText().toString().trim();
        final String LastName = editTextLastName.getText().toString().trim();
        final String Email = editTextEmail.getText().toString().trim();
        final String Password = editTextPassword.getText().toString().trim();
        final String Owner;
        if (flag == true) {
            Owner = "yes";
        } else {
            Owner = "no";
        }


        if (FirstName.isEmpty()) {
            editTextFirstName.setError("First Name Required");
            editTextFirstName.requestFocus();
            return;
        }
        if (LastName.isEmpty()) {
            editTextLastName.setError("Last Name Required");
            editTextLastName.requestFocus();
            return;
        }
        if (Email.isEmpty()) {
            editTextEmail.setError("Email Required");
            editTextEmail.requestFocus();
            return;
        }
        if (Password.isEmpty()) {
            editTextPassword.setError("password is required");
            editTextPassword.requestFocus();
            return;
        }
        if (Password.length() < 8) {
            editTextPassword.setError("password length should be higher than 8");
            editTextPassword.requestFocus();
            return;
        }

        authFB.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User_Form user = new User_Form(FirstName, Email, LastName, Password, Owner);
                            FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getUid()).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignUp.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                                                if (flag == false) {
                                                    Intent i = new Intent(SignUp.this, Login.class);
                                                    startActivity(i);
                                                } else {
                                                    Intent i = new Intent(SignUp.this, Owner_Detail.class);
                                                    startActivity(i);
                                                }
                                            } else {

                                                Toast.makeText(SignUp.this, "Error, couldn't sign up", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpButton:
                registerUser();
                break;

            case R.id.goToLogInText:
                Intent i = new Intent(SignUp.this, Login.class);
                startActivity(i);
                break;

        }
    }

}
package com.example.myapplication.auth;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.admin.Admin;
import com.example.myapplication.admin.Reg;
import com.example.myapplication.Services;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText emailId;
    EditText password;
    Button btnSignIn;
    TextView tvSignUp;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        btnSignIn = findViewById(R.id.button2);
        tvSignUp = findViewById(R.id.tv_login);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void signInUser() {
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        loader = findViewById(R.id.reg_progress);

        String mail = emailId.getText().toString().trim();
        String passw = password.getText().toString().trim();

        if (mail.isEmpty()) {
            emailId.setError("Please Enter Email");
            emailId.requestFocus();
            return;
        }
        if (passw.isEmpty()) {
            password.setError("Please Enter Password");
            password.requestFocus();
            return;
        }
        if (passw.length()<6) {
            password.setError("Password length should be 6");
            password.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            emailId.setError("Enter a Valid email");
            emailId.requestFocus();
            return;
        }loader.setVisibility(View.VISIBLE);

        btnSignIn.setClickable(false);
       mAuth.signInWithEmailAndPassword(mail, passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               loader.setVisibility(View.GONE);
               btnSignIn.setClickable(true);


               if (task.isSuccessful()) {
                   //check if is admin
                  String admin = mAuth.getUid();
                  String IsAdmin = "mskEczZSSnQH0DEsdaEHOaVbpaI2";
                  if(admin.matches("NXKN1BArRygzu2nKQvZ6bFT5day1") || admin.matches(IsAdmin)){
                      Intent intent = new Intent(LoginActivity.this, Admin.class);
                      startActivity(intent);
                      finish();
                  }else{
                   Toast.makeText(LoginActivity.this,"Login successful",Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(LoginActivity.this, Services.class);
                   startActivity(intent);
                   finish(); //Kill login activity to avoid back-pressing to the login activity.
                  }
               }  else{
                   Toast.makeText(LoginActivity.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                   Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
               }
           }
       });
    }

}


package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText Email;
    EditText Password1;
    EditText Password2;
    TextView ToLogin;
    Button reg;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        ToLogin = findViewById(R.id.tv_login);
        reg = findViewById(R.id.btn_reg);

        ToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });
    }

    private void RegisterUser() {
        Email = findViewById(R.id.email_nw);
        Password1 = findViewById(R.id.password1);
        Password2 = findViewById(R.id.password2);
        loader = findViewById(R.id.reg_progress);

        String mail = Email.getText().toString().trim();
        String pssw1 = Password1.getText().toString().trim();
        String pssw2 = Password2.getText().toString().trim();

        if (mail.isEmpty()) {
            Email.setError("Please Enter Email");
            Email.requestFocus();
            return;
        }
        if (pssw1.isEmpty()) {
            Password1.setError("Please Enter Password");
            Password1.requestFocus();
            return;
        }
        if (pssw2.isEmpty()) {
            Password2.setError("Please Enter Password");
            Password2.requestFocus();
            return;
        }
        if (pssw1.length()<6) {
            Password1.setError("Password length should be 6");
            Password1.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            Email.setError("Enter a Valid email");
            Email.requestFocus();
            return;
        }
        if (!pssw1.matches(pssw2)) {
            Password1.setError("The Two Passwords Do Not Match");
            Password1.requestFocus();
            return;
        }
        loader.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(mail, pssw1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loader.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Registration successful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, Services.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(SignUp.this, "Registration Unsuccessful",Toast.LENGTH_SHORT).show();
                    Toast.makeText(SignUp.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

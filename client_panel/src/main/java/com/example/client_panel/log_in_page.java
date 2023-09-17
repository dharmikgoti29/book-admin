package com.example.client_panel;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class log_in_page extends AppCompatActivity {

    TextView signup;
    Button login;

    EditText email,pass;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        signup=findViewById(R.id.txtSignup);
        login=findViewById(R.id.btnLogin);
        email=findViewById(R.id.edtusername);
        pass=findViewById(R.id.edtupass);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().isEmpty() || pass.getText().toString().isEmpty())
                {
                    Toast.makeText(log_in_page.this, "Enter Text first", Toast.LENGTH_SHORT).show();
                }
                else {
                    login(email.getText().toString(),pass.getText().toString());
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(log_in_page.this,registration.class);
                startActivity(intent);

            }
        });


    }

    private void login(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(log_in_page.this, client_dashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
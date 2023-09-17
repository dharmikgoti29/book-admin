package com.example.yourstore_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registration_page extends AppCompatActivity {

    EditText name,email,password,confirmpassword,age;
    Button register;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userid;

    String sname,sage,semail,spassword,scon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        name=findViewById(R.id.edtname);
        age=findViewById(R.id.edtDate);
        email=findViewById(R.id.edtemailaddress);
        password=findViewById(R.id.edtpassword1);
        confirmpassword=findViewById(R.id.edtpassword2);
        register=findViewById(R.id.btnRegisterUser);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sname=name.getText().toString();
                sage=age.getText().toString();
                semail=email.getText().toString();
                spassword=password.getText().toString();
                scon=confirmpassword.getText().toString();
                if(sname.isEmpty() || sage.isEmpty() || semail.isEmpty() || spassword.isEmpty() || scon.isEmpty())
                {
                    Toast.makeText(registration_page.this, "Enter Text first", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    register(email.getText().toString(),password.getText().toString());
                }
            }
        });

    }

    private void register(String toString, String toString1) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(toString,toString1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = auth.getCurrentUser();
                    if(user!=null)
                    {
                        userid=user.getUid();
                        Map<String,Object> user_detail = new HashMap<>();
                        user_detail.put("name",sname);
                        user_detail.put("age",sage);
                        user_detail.put("email",semail);
                        user_detail.put("userid",userid);

                        db.collection("user_detail").document(userid).set(user_detail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent intent = new Intent(registration_page.this, client_dashboard.class);
                                startActivity(intent);
                                Toast.makeText(registration_page.this, "Register successfully", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }
            }
        });
    }
}
package com.example.yourestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCategory extends AppCompatActivity {

    //activity back
    ImageButton btn_back;
    TextInputLayout inputLayout;
    EditText addcat;
    Button add;

    String catagory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        btn_back = findViewById(R.id.btnBack);
        inputLayout = findViewById(R.id.edt_email);
        addcat=inputLayout.getEditText();
        add = findViewById(R.id.btnADD);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message");

                if(addcat != null)
                {
                    catagory = addcat.getText().toString();
                    if(catagory.isEmpty())
                    {
                        Toast.makeText(AddCategory.this, "add text", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        myRef.push().setValue(catagory).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddCategory.this, "catagory added successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddCategory.this, "failed to add catagory", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
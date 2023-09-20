package com.example.yourestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCategory extends AppCompatActivity {

    //activity back
    ImageButton btn_back;
    TextInputLayout inputLayout;
    EditText addcat;
    Button add;
    private ProgressBar progressBar;

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
                DatabaseReference myRef = database.getReference("subjects");

                if(addcat != null)
                {
                    catagory = addcat.getText().toString();
                    if(catagory.isEmpty())
                    {
                        Toast.makeText(AddCategory.this, "Please add text", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String tm_cat = addcat.getText().toString().trim().toLowerCase();
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Boolean check = false;
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    String av_cat = snapshot1.getValue().toString().trim().toLowerCase();
                                    if (av_cat.equals(tm_cat)) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (check == true) {
                                    Toast.makeText(AddCategory.this, "Catagory is already available", Toast.LENGTH_SHORT).show();
                                } else if (check == false) {
                                    myRef.push().setValue(catagory).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(AddCategory.this, "catagory added successfully", Toast.LENGTH_SHORT).show();
                                            onBackPressed();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddCategory.this, "failed to add catagory", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

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
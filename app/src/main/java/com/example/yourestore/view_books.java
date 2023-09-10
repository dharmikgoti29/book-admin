package com.example.yourestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class view_books extends AppCompatActivity {

    Button addcat,addbook;
    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("subjects");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);

        addcat=findViewById(R.id.btn_add_categories);
        addbook=findViewById(R.id.btn_add_pdf);
        recyclerView=findViewById(R.id.categoriesList);

        addcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view_books.this, AddCategory.class);
                startActivity(intent);
            }
        });
        addbook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view_books.this,AddBooks.class);
                        startActivity(intent);
                    }
        });

        ArrayList<model> recycle_list = new ArrayList<>();

        recycleadapter recycleadapter = new recycleadapter(this,recycle_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recycleadapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                recycle_list.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    model model = new model(snapshot1.getValue().toString());
                    recycle_list.add(model);
                }
                recycleadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}